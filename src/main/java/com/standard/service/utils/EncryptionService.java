package com.standard.service.utils;

import com.standard.service.config.PiiProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Locale;

@Component
public class EncryptionService {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    private final SecretKey key;
    private final String hashPepper;
    private final int hashPepperVersion;

    public EncryptionService(PiiProperties properties) {
        String secretKeyStr = properties.getEncryptionKey();
        if (secretKeyStr == null || secretKeyStr.length() < 32) {
            secretKeyStr = "Default32ByteLongSecureKey123456";
        }
        this.key = new SecretKeySpec(secretKeyStr.substring(0, 32).getBytes(StandardCharsets.UTF_8), "AES");

        this.hashPepper = properties.getHashPepper() != null ? properties.getHashPepper()
                : "Default32ByteLongSecurePepper123";
        this.hashPepperVersion = properties.getHashPepperVersion() > 0 ? properties.getHashPepperVersion() : 1;
    }

    public String encrypt(String raw) {
        if (raw == null)
            return null;
        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            byte[] cipherText = cipher.doFinal(raw.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("AES/GCM Encryption failed", e);
        }
    }

    public String decrypt(String encryptedBase64) {
        if (encryptedBase64 == null)
            return null;
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedBase64);
            if (combined.length < IV_LENGTH)
                return encryptedBase64;

            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);

            byte[] cipherText = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, IV_LENGTH, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return encryptedBase64;
        }
    }

    public String generateBlindIndex(String raw) {
        if (raw == null || raw.isBlank())
            return null;

        String normalized = raw.trim().toLowerCase(Locale.ROOT);
        try {
            byte[] derivedKey = deriveKeyWithHKDF(this.hashPepper.getBytes(StandardCharsets.UTF_8));
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(derivedKey, "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(normalized.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("HMAC hashing failed", e);
        }
    }

    private byte[] deriveKeyWithHKDF(byte[] ikm) throws Exception {
        Mac extractMac = Mac.getInstance("HmacSHA256");
        byte[] salt = new byte[32]; // Standard zero salt
        extractMac.init(new SecretKeySpec(salt, "HmacSHA256"));
        byte[] prk = extractMac.doFinal(ikm);

        Mac expandMac = Mac.getInstance("HmacSHA256");
        expandMac.init(new SecretKeySpec(prk, "HmacSHA256"));
        expandMac.update("blind-index".getBytes(StandardCharsets.UTF_8));
        expandMac.update((byte) 1);
        return expandMac.doFinal();
    }
}
