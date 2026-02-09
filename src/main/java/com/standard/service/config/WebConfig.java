package com.standard.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Enterprise Web Configuration. Handles Internationalization (i18n), CORS
 * policies, and Web Interceptors.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @org.springframework.beans.factory.annotation.Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @org.springframework.beans.factory.annotation.Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS,PATCH}")
    private String allowedMethods;

    @org.springframework.beans.factory.annotation.Value("${app.cors.allowed-headers:*}")
    private String allowedHeaders;

    @org.springframework.beans.factory.annotation.Value("${app.cors.max-age:3600}")
    private long maxAge;

    /**
     * Configures the LocaleResolver to use the 'Accept-Language' header. Default is
     * set to US English.
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    /**
     * Interceptor to allow switching locales via a 'lang' parameter (e.g.,
     * ?lang=fr).
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Standard CORS configuration. Values are read directly from application.yml
     * using @Value for a leaner approach.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowedOrigins.split(",")).allowedMethods(allowedMethods.split(","))
                .allowedHeaders(allowedHeaders.split(",")).maxAge(maxAge);
    }
}
