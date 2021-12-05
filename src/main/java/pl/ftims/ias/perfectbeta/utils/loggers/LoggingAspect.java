package pl.ftims.ias.perfectbeta.utils.loggers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LogManager.getLogger(this.getClass());


    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }


    @Pointcut("within(pl.ftims.ias.perfectbeta.mok..*)" +
            " || within(pl.ftims.ias.perfectbeta.mok.endpoints..*)" +
            " || within(pl.ftims.ias.perfectbeta.mok.services..*)" +
            " || within(pl.ftims.ias.perfectbeta.mok.repositories..*)" +
            " || within(pl.ftims.ias.perfectbeta.mos.services..*)" +
            " || within(pl.ftims.ias.perfectbeta.mos.endpoints..*)" +
            " || within(pl.ftims.ias.perfectbeta.auth.repositories..*)" +
            " || within(pl.ftims.ias.perfectbeta.auth.endpoints..*)" +
            " || within(pl.ftims.ias.perfectbeta.auth.security..*)" +
            " || within(pl.ftims.ias.perfectbeta.moch.endpoints..*)" +
            " || within(pl.ftims.ias.perfectbeta.moch.services..*)"
    )
    public void applicationPackagePointcut() {
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }

    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("{} {}  Enter: {}.{}() with argument[s] = {}", auth.getName(), auth.getAuthorities(), joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), ((MethodSignature) joinPoint.getSignature()).getParameterNames());

        try {
            Object result = joinPoint.proceed();

            log.info("{} {} Exit: {}.{}() with result = {}", auth.getName(), auth.getAuthorities(), joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("{} {} Illegal argument: {} in {}.{}()", auth.getName(), auth.getAuthorities(), Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
