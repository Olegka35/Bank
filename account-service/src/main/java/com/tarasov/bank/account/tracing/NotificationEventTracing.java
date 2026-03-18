package com.tarasov.bank.account.tracing;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationEventTracing {
    private final Tracer tracer;

    @Around("execution(* com.tarasov.bank.account.producer.KafkaNotificationProducer.sendNotification(..))")
    public Object traceRepositoryMethods(ProceedingJoinPoint pjp) throws Throwable {
        var span = tracer.nextSpan().name("Notification sending").start();
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            span.error(throwable);
            throw throwable;
        } finally {
            span.end();
        }
    }
}
