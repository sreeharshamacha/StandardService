package com.standard.service.converter;

import com.standard.service.config.ApplicationContextProvider;
import com.standard.service.utils.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PiiAttributeConverter implements AttributeConverter<String, String> {

    private EncryptionService getEncryptionService() {
        return ApplicationContextProvider.getBean(EncryptionService.class);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return attribute;
        }
        return getEncryptionService().encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return dbData;
        }
        return getEncryptionService().decrypt(dbData);
    }
}
