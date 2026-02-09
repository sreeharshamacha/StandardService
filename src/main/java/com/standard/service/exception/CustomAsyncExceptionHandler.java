package com.standard.service.exception;

import com.standard.service.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Centralized handler for exceptions thrown by asynchronous methods (@Async)
 * with 'void' return type.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final MessageUtils messageUtils;

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        String message = messageUtils.getMessage(ErrorCode.ASYNC_EXECUTION_ERROR.getMessageKey(), method.getName());
        log.error(message);
        log.error("Exception detail: {}", ex.getMessage());

        if (params != null && params.length > 0) {
            for (Object param : params) {
                log.error("Parameter value: {}", param);
            }
        }
    }
}
