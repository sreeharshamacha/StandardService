package com.standard.service.config;

import com.standard.service.exception.CustomAsyncExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @org.springframework.beans.factory.annotation.Value("${app.async.core-pool-size:5}")
    private int corePoolSize;

    @org.springframework.beans.factory.annotation.Value("${app.async.max-pool-size:10}")
    private int maxPoolSize;

    @org.springframework.beans.factory.annotation.Value("${app.async.queue-capacity:500}")
    private int queueCapacity;

    @org.springframework.beans.factory.annotation.Value("${app.async.thread-name-prefix:StandardService-Async-}")
    private String threadNamePrefix;

    private final CustomAsyncExceptionHandler customAsyncExceptionHandler;

    public AsyncConfig(CustomAsyncExceptionHandler customAsyncExceptionHandler) {
        this.customAsyncExceptionHandler = customAsyncExceptionHandler;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return customAsyncExceptionHandler;
    }

    /**
     * Propagates MDC context from the main thread to async threads.
     */
    static class MdcTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    if (contextMap != null) {
                        MDC.setContextMap(contextMap);
                    }
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }
}
