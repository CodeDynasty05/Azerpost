package com.azerpost.app.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Aspect
@Component
@Order(1)
public class ServiceLoggingAspect {

    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logServiceInvocation(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        String method = joinPoint.getSignature().toShortString();

        try {
            Object result = joinPoint.proceed();
            log.debug("{} completed in {} ms", method, Duration.between(start, Instant.now()).toMillis());
            return result;
        } catch (Throwable ex) {
            log.error("{} failed after {} ms", method, Duration.between(start, Instant.now()).toMillis(), ex);
            throw ex;
        }
    }
}
