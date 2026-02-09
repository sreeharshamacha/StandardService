package com.standard.service.exception;

import lombok.Getter;

/**
 * Base exception class for all custom service exceptions.
 */
@Getter
public class ServiceException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] args;

    public ServiceException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.args = args;
    }

    public ServiceException(String message, ErrorCode errorCode, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = args;
    }
}
