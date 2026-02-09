package com.standard.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.standard.service.repository")
@EnableTransactionManagement
@EnableJpaAuditing
public class JpaConfig {
    // Additional Hibernate or JPA customization can be added here
}
