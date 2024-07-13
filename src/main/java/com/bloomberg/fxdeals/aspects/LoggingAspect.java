package com.bloomberg.fxdeals.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Before(value = "@annotation(ToLog)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @After(value = "@annotation(ToLog)")
    public void logAfter(JoinPoint joinPoint) {
        log.info("Exiting method: {}", joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "@annotation(ToLog)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method {} returned with value {}", joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "@annotation(ToLog)", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("Method {} threw exception {}", joinPoint.getSignature().toShortString(), error);
    }
}
