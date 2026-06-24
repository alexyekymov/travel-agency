package dev.overlax.agency.aspect;

import dev.overlax.agency.security.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllers() {
    }

    @After("controllers()")
    public void logControllerCall(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String method = joinPoint.getSignature().getName();

        log.debug("Controller method {}.{} was called by user={}", className, method, currentUser());
    }

    private String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "anonymous";
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof AuthUser authUser) {
            return authUser.getEmail();
        }

        return String.valueOf(principal);
    }
}
