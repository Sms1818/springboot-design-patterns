package com.example.fileprocessing.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.fileprocessing..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Starting method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.example.fileprocessing..*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("Completed method: " +
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.example.fileprocessing..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        System.out.println("Exception in method: "
                + joinPoint.getSignature().getName()
                + " | Message: "
                + ex.getMessage());
    }

    @Around("execution(* com.example.fileprocessing..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.println(
                "Execution time of "
                        + joinPoint.getSignature().getName()
                        + ": "
                        + (executionTime)
                        + " ms");

        return result;
    }

}
