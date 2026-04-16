package com.standard.service.aop;

import com.standard.service.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.standard.service.controller..*(..)) || "
            + "execution(* com.standard.service.service..*(..)) || " + "execution(* com.standard.service.saga..*(..))")
    public void applicationPointcut() {
    }

    @Around("applicationPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        log.info(AppConstants.LOG_ENTRY, methodName, Arrays.toString(joinPoint.getArgs()));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.info(AppConstants.LOG_EXIT, methodName, result);
            log.info(AppConstants.LOG_EXECUTION_TIME, methodName, executionTime);

            return result;
        } catch (Throwable e) {
            log.error(AppConstants.LOG_EXCEPTION, className, methodName, e.getMessage());
            throw e;
        }
    }

    @AfterThrowing(pointcut = "applicationPointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error(AppConstants.LOG_EXCEPTION, joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), ex.getMessage());
    }
}
