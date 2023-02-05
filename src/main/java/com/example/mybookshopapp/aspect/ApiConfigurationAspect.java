package com.example.mybookshopapp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApiConfigurationAspect {

    @Around(value = "@annotation(com.example.mybookshopapp.aspect.annotation.ApiConfiguration)")
    public Object checkingConfigurationParameters(ProceedingJoinPoint proceedingJoinPoint) {
        Object object;
        try {
            object = proceedingJoinPoint.proceed();
            if (object == null) {
                log.warn("Не заданы настройки для класса {}", proceedingJoinPoint.getTarget().getClass().getSimpleName());
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return object;
    }
}
