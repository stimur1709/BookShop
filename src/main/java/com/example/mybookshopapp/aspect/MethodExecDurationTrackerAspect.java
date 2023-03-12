package com.example.mybookshopapp.aspect;

import com.example.mybookshopapp.service.userService.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MethodExecDurationTrackerAspect {

    private final UserProfileService userProfileService;

    @Autowired
    public MethodExecDurationTrackerAspect(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Around(value = "@annotation(com.example.mybookshopapp.aspect.annotation.DurationTrackable)")
    public Object markedLogExecutionTimeAnnotationAround(ProceedingJoinPoint joinPoint) {
        long durationMills = System.currentTimeMillis();
        Object returnedValue = null;
        try {
            returnedValue = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (userProfileService.isAuthenticatedUser()) {
            String hash = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("Method {} execution time is {} ms, user is {}", joinPoint.getSignature().getName(), System.currentTimeMillis() - durationMills, hash);
        } else {
            log.info("Method {} execution time is {} ms, user not auth", joinPoint.getSignature().getName(), System.currentTimeMillis() - durationMills);
        }
        return returnedValue;
    }
}
