package com.standard.service.config;

import com.standard.service.interceptor.PiiHibernateInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PiiHibernateConfig implements HibernatePropertiesCustomizer {

    private final PiiHibernateInterceptor piiHibernateInterceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", piiHibernateInterceptor);
    }
}
