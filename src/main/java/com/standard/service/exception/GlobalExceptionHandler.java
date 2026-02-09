package com.standard.service.exception;

import com.standard.service.dto.ApiResponse;
import com.standard.service.utils.MdcUtils;
import com.standard.service.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageUtils messageUtils;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex, WebRequest request) {
        String message = messageUtils.getMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessageKey());
        ApiResponse<Object> response = ApiResponse.error(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                message,
                MdcUtils.getTraceId());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<Object>> handleServiceException(ServiceException ex, WebRequest request) {
        String message = messageUtils.getMessage(ex.getErrorCode().getMessageKey(), ex.getArgs());
        ApiResponse<Object> response = ApiResponse.error(
                ex.getErrorCode().getCode(),
                message,
                MdcUtils.getTraceId());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        String message = messageUtils.getMessage(ex.getErrorCode().getMessageKey(), ex.getArgs());
        ApiResponse<Object> response = ApiResponse.error(
                ex.getErrorCode().getCode(),
                message,
                MdcUtils.getTraceId());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
