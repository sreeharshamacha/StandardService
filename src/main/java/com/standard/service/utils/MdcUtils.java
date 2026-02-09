package com.standard.service.utils;

import com.standard.service.constant.AppConstants;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Utility class for centralized MDC context management.
 */
public final class MdcUtils {

    private MdcUtils() {
        // Prevent instantiation
    }

    public static String getTraceId() {
        return MDC.get(AppConstants.TRACE_ID);
    }

    public static String getSpanId() {
        return MDC.get(AppConstants.SPAN_ID);
    }

    public static void setTraceId(String traceId) {
        MDC.put(AppConstants.TRACE_ID, traceId);
    }

    public static void setSpanId(String spanId) {
        MDC.put(AppConstants.SPAN_ID, spanId);
    }

    public static void clear() {
        MDC.clear();
    }

    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
