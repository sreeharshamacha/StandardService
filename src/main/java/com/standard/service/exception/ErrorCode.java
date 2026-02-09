package com.standard.service.exception;

import lombok.Getter;

/**
 * Enumeration for standardized error codes across the service.
 */
@Getter
public enum ErrorCode {

    // General Errors
    INTERNAL_SERVER_ERROR("ERR-001", "internal.server.error"),
    INVALID_REQUEST("ERR-002", "invalid.request"),

    // Resource Specific Errors
    RESOURCE_NOT_FOUND("RES-404", "resource.not.found"),
    RESOURCE_ALREADY_EXISTS("RES-409", "resource.already.exists"),

    // Resilience & Service Errors
    RATE_LIMIT_EXCEEDED("RL-429", "rate.limit.exceeded"),
    BULKHEAD_FULL("BH-503", "bulkhead.full"),
    EXTERNAL_SERVICE_ERROR("EXT-500", "external.service.error"),
    SERVICE_UNAVAILABLE("SVC-503", "service.unavailable"),

    // Async Errors
    ASYNC_EXECUTION_ERROR("ASY-001", "async.execution.error");

    private final String code;
    private final String messageKey;

    ErrorCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
