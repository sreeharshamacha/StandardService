package com.standard.service.constant;

public final class AppConstants {

    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String API_V1_PREFIX = "/api/v1";
    public static final String TRACE_ID = "traceId";
    public static final String SPAN_ID = "spanId";
    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MSG = "Success";

    // Logging patterns
    public static final String LOG_ENTRY = "Entering method: {} with arguments: {}";
    public static final String LOG_EXIT = "Exiting method: {} with result: {}";
    public static final String LOG_EXECUTION_TIME = "Method {} executed in {} ms";
    public static final String LOG_EXCEPTION = "Exception in {}.{} with cause: {}";

    // Cache names
    public static final String CACHE_RESOURCES = "resources";
    public static final String CACHE_USERS = "users";
    public static final String CACHE_PRODUCTS = "products";
    public static final String CACHE_SETTINGS = "settings";
    public static final String HAZELCAST_INSTANCE = "hazelcast-instance";
    public static final String STANDARD_CACHE = "standard-cache";

    // Security
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String SYSTEM_USER = "SYSTEM";
    public static final String JWT_PREFERRED_USERNAME = "preferred_username";
}
