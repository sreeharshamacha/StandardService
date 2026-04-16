package com.standard.service.exception;

import com.standard.service.utils.MdcUtils;
import com.standard.service.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageUtils messageUtils;

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex) {
        String message = messageUtils.getMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessageKey());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, message);
        problemDetail.setTitle(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        problemDetail.setProperty("traceId", MdcUtils.getTraceId());
        return problemDetail;
    }

    @ExceptionHandler(ServiceException.class)
    public ProblemDetail handleServiceException(ServiceException ex) {
        String message = messageUtils.getMessage(ex.getErrorCode().getMessageKey(), ex.getArgs());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        problemDetail.setTitle(ex.getErrorCode().getCode());
        problemDetail.setProperty("traceId", MdcUtils.getTraceId());
        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {
        String message = messageUtils.getMessage(ex.getErrorCode().getMessageKey(), ex.getArgs());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        problemDetail.setTitle(ex.getErrorCode().getCode());
        problemDetail.setProperty("traceId", MdcUtils.getTraceId());
        return problemDetail;
    }
}
