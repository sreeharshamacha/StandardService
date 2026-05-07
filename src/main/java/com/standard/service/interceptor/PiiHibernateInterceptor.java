package com.standard.service.interceptor;

import com.standard.service.config.PiiProperties;
import com.standard.service.utils.EncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class PiiHibernateInterceptor implements Interceptor {

    private final PiiProperties piiProperties;
    private final EncryptionService encryptionService;
    private final Map<String, Set<String>> cachedPiiFields = new ConcurrentHashMap<>();

    @Override
    public boolean onLoad(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types)
            throws CallbackException {
        return processState(entity.getClass().getName(), state, propertyNames, false);
    }

    @Override
    public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types)
            throws CallbackException {
        return processState(entity.getClass().getName(), state, propertyNames, true);
    }

    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) throws CallbackException {
        return processState(entity.getClass().getName(), currentState, propertyNames, true);
    }

    private boolean processState(String className, Object[] state, String[] propertyNames, boolean isEncrypt) {
        if (piiProperties.getEntities() == null || !piiProperties.getEntities().containsKey(className)) {
            return false;
        }

        Set<String> piiFields = cachedPiiFields.computeIfAbsent(className,
                k -> new HashSet<>(piiProperties.getEntities().get(k)));
        boolean modified = false;

        for (int i = 0; i < propertyNames.length; i++) {
            if (piiFields.contains(propertyNames[i]) && state[i] instanceof String) {
                String value = (String) state[i];
                if (value != null && !value.isEmpty()) {
                    try {
                        if (isEncrypt) {
                            state[i] = encryptionService.encrypt(value);
                            
                            String hashFieldName = propertyNames[i] + "Hash";
                            setHashState(propertyNames, state, hashFieldName, encryptionService.generateBlindIndex(value));
                            
                            String hashVersionFieldName = propertyNames[i] + "HashVersion";
                            setHashState(propertyNames, state, hashVersionFieldName, piiProperties.getHashPepperVersion());
                            
                            modified = true;
                        } else {
                            state[i] = encryptionService.decrypt(value);
                            modified = true;
                        }
                    } catch (Exception e) {
                        log.error("Failed to process PII field {} on entity {}", propertyNames[i], className, e);
                    }
                }
            }
        }
        return modified;
    }

    private void setHashState(String[] propertyNames, Object[] state, String hashFieldName, Object hashValue) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyNames[i].equals(hashFieldName)) {
                state[i] = hashValue;
                break;
            }
        }
    }
}
