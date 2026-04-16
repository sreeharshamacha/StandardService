package com.standard.service.utils;

import com.standard.service.constant.AppConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;

class MdcUtilsTest {

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void setAndGetTraceId() {
        String testId = "trace-123";
        MdcUtils.setTraceId(testId);

        assertEquals(testId, MdcUtils.getTraceId());
        assertEquals(testId, MDC.get(AppConstants.TRACE_ID));
    }

    @Test
    void setAndGetSpanId() {
        String testSpan = "span-456";
        MdcUtils.setSpanId(testSpan);

        assertEquals(testSpan, MdcUtils.getSpanId());
        assertEquals(testSpan, MDC.get(AppConstants.SPAN_ID));
    }

    @Test
    void generateId_LengthAndAlphanumeric() {
        String generated = MdcUtils.generateId();
        assertNotNull(generated);
        assertEquals(16, generated.length());
        assertFalse(generated.contains("-"));
    }

    @Test
    void clear_EmptiesMdc() {
        MdcUtils.setTraceId("trace-123");
        MdcUtils.clear();
        assertNull(MdcUtils.getTraceId());
    }
}
