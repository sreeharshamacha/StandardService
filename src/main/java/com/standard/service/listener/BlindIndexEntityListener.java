package com.standard.service.listener;

import com.standard.service.config.ApplicationContextProvider;
import com.standard.service.config.PiiProperties;
import com.standard.service.converter.PiiAttributeConverter;
import com.standard.service.utils.EncryptionService;
import jakarta.persistence.Convert;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BlindIndexEntityListener {

    private static final Map<Class<?>, List<Field>> cachedPiiFields = new ConcurrentHashMap<>();

    private EncryptionService getEncryptionService() {
        return ApplicationContextProvider.getBean(EncryptionService.class);
    }

    private PiiProperties getPiiProperties() {
        return ApplicationContextProvider.getBean(PiiProperties.class);
    }

    @PrePersist
    @PreUpdate
    public void processBlindIndex(Object entity) {
        if (entity == null) return;

        Class<?> entityClass = entity.getClass();
        List<Field> piiFields = cachedPiiFields.computeIfAbsent(entityClass, this::scanForPiiFields);

        if (piiFields.isEmpty()) return;

        EncryptionService encryptionService = getEncryptionService();
        PiiProperties piiProperties = getPiiProperties();

        if (encryptionService == null || piiProperties == null) {
            log.warn("Encryption components not yet initialized. Skipping blind index generation for {}", entityClass.getSimpleName());
            return;
        }

        for (Field field : piiFields) {
            try {
                ReflectionUtils.makeAccessible(field);
                Object value = ReflectionUtils.getField(field, entity);

                if (value instanceof String) {
                    String strValue = (String) value;
                    if (!strValue.isEmpty()) {
                        String blindIndex = encryptionService.generateBlindIndex(strValue);
                        setFieldValue(entity, entityClass, field.getName() + "Hash", blindIndex);
                        setFieldValue(entity, entityClass, field.getName() + "HashVersion", piiProperties.getHashPepperVersion());
                    }
                }
            } catch (Exception e) {
                log.error("Error setting blind index for field {} on entity {}", field.getName(), entityClass.getSimpleName(), e);
            }
        }
    }

    private List<Field> scanForPiiFields(Class<?> clazz) {
        List<Field> piiFields = new ArrayList<>();
        ReflectionUtils.doWithFields(clazz, field -> {
            Convert convertAnnotation = field.getAnnotation(Convert.class);
            if (convertAnnotation != null && convertAnnotation.converter() == PiiAttributeConverter.class) {
                piiFields.add(field);
            }
        });
        return piiFields;
    }

    private void setFieldValue(Object entity, Class<?> entityClass, String fieldName, Object value) {
        Field field = ReflectionUtils.findField(entityClass, fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, entity, value);
        }
    }
}
