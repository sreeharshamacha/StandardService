package com.standard.service.interceptor;

import com.standard.service.config.PiiProperties;
import com.standard.service.utils.EncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PiiHibernateInterceptor implements Interceptor {

    private final PiiProperties piiProperties;
    private final EncryptionService encryptionService;

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

        List<String> piiFields = piiProperties.getEntities().get(className);
        boolean modified = false;

        for (int i = 0; i < propertyNames.length; i++) {
            if (piiFields.contains(propertyNames[i]) && state[i] instanceof String) {
                String value = (String) state[i];
                if (value != null && !value.isEmpty()) {
                    try {
                        if (isEncrypt) {
                            state[i] = encryptionService.encrypt(value);
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
}
