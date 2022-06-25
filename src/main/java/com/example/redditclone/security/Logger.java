package com.example.redditclone.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {

    @AfterReturning(value = "execution(* com..*Bean.*(..))")
    public void logAfterServiceAccess(JoinPoint joinPoint) {
        System.out.println("Method Completed: " + joinPoint);
    }

    @Before(value = "execution(* com..*Bean.*(..))")
    public void logBeforeServiceAccess(JoinPoint joinPoint) {
        System.out.println("Method Started: " + joinPoint);
    }
}
