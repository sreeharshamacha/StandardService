package com.standard.service.saga.core;

public class SagaExecutionException extends RuntimeException {
    public SagaExecutionException(String message) {
        super(message);
    }

    public SagaExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
