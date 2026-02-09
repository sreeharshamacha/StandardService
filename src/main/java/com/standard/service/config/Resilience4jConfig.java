package com.standard.service.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Programmatic configuration for Resilience4j. Complements application.yml by
 * adding event listeners for better observability.
 */
@Slf4j
@Configuration
public class Resilience4jConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(5)).build();
    }

    /**
     * Custom Circuit Breaker Registry Event Consumer. Logs all circuit breaker
     * registrations for monitoring.
     */
    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerEventConsumer() {
        return new RegistryEventConsumer<>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                log.info("Circuit Breaker added: {}", entryAddedEvent.getAddedEntry().getName());
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                log.info("Circuit Breaker removed: {}", entryRemoveEvent.getRemovedEntry().getName());
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("Circuit Breaker replaced: {} -> {}", entryReplacedEvent.getOldEntry().getName(),
                        entryReplacedEvent.getNewEntry().getName());
            }
        };
    }

    /**
     * Configure Circuit Breaker Registry with event listeners.
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(RegistryEventConsumer<CircuitBreaker> eventConsumer) {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(CircuitBreakerConfig.ofDefaults(), eventConsumer);

        // Add event listeners for the 'mainService' configured in application.yml
        registry.circuitBreaker("mainService").getEventPublisher()
                .onStateTransition(event -> log.warn("Circuit Breaker 'mainService' State Transition: {} -> {}",
                        event.getStateTransition().getFromState(), event.getStateTransition().getToState()))
                .onError(event -> log.error("Circuit Breaker 'mainService' Error: {}",
                        event.getThrowable().getMessage()))
                .onSuccess(event -> log.debug("Circuit Breaker 'mainService' Success: duration={}ms",
                        event.getElapsedDuration().toMillis()));

        return registry;
    }

    /**
     * Configure Retry Registry with event listeners.
     */
    @Bean
    public RetryRegistry retryRegistry() {
        RetryRegistry registry = RetryRegistry.ofDefaults();

        registry.retry("mainService").getEventPublisher().onRetry(
                event -> log.warn("Retry attempt #{} for: {}", event.getNumberOfRetryAttempts(), event.getName()))
                .onError(event -> log.error("Retry failed after {} attempts: {}", event.getNumberOfRetryAttempts(),
                        event.getLastThrowable().getMessage()));

        return registry;
    }

    /**
     * Configure Rate Limiter Registry with event listeners.
     */
    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        RateLimiterRegistry registry = RateLimiterRegistry.ofDefaults();

        registry.rateLimiter("mainService").getEventPublisher()
                .onSuccess(event -> log.debug("Rate Limiter 'mainService' Success: available permissions={}",
                        event.getNumberOfPermits()))
                .onFailure(event -> log.warn("Rate Limiter 'mainService' Failure: no permissions available"));

        return registry;
    }

    /**
     * Configure Bulkhead Registry with event listeners.
     */
    @Bean
    public BulkheadRegistry bulkheadRegistry() {
        BulkheadRegistry registry = BulkheadRegistry.ofDefaults();

        registry.bulkhead("mainService").getEventPublisher()
                .onCallPermitted(event -> log.debug("Bulkhead 'mainService' Call Permitted"))
                .onCallRejected(event -> log.warn("Bulkhead 'mainService' Call Rejected"));

        return registry;
    }
}
