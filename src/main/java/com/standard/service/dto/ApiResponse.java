package com.standard.service.dto;

import com.standard.service.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Unified API Response wrapper for all service endpoints.
 * Provides a consistent structure for success and error responses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String traceId;

    /**
     * Manual multi-arg constructor for specific use cases.
     */
    public ApiResponse(String code, String message, T data, String traceId) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.traceId = traceId;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return ApiResponse.<T>builder()
                .code(AppConstants.SUCCESS_CODE)
                .message(AppConstants.SUCCESS_MSG)
                .data(data)
                .traceId(traceId)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, String traceId) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .traceId(traceId)
                .build();
    }
}
