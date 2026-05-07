package com.standard.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "pii")
public class PiiProperties {
    private String encryptionKey;
    private int hashPepperVersion = 1;
    private String hashPepper;
    private Map<String, List<String>> entities;
}
