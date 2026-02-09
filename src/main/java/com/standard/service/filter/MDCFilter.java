package com.standard.service.filter;

import com.standard.service.constant.AppConstants;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class MDCFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    public MDCFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (tracer != null && tracer.currentSpan() != null) {
            String traceId = tracer.currentSpan().context().traceId();
            String spanId = tracer.currentSpan().context().spanId();

            MDC.put(AppConstants.TRACE_ID, traceId);
            MDC.put(AppConstants.SPAN_ID, spanId);

            // Also add to response headers for debugging
            response.addHeader(AppConstants.TRACE_ID, traceId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(AppConstants.TRACE_ID);
            MDC.remove(AppConstants.SPAN_ID);
        }
    }
}
