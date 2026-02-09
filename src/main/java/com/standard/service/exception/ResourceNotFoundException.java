package com.standard.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ServiceException {
    public ResourceNotFoundException(Object... args) {
        super(ErrorCode.RESOURCE_NOT_FOUND, args);
    }
}
