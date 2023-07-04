package ru.clevertec.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <p>An aspect class that adds functionality for logging the repository layer and the service layer.</p>
 *
 * @author Artur Malashkov
 * @since 17
 */
@Slf4j
@Aspect
public class LoggingJPARepositoryAndServiceLayer {

    @Pointcut("within(org.springframework.data.jpa.repository.JpaRepository+)")
    public void pointCutForJPARepository() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service*)")
    public void pointCutForServiceLayer() {
    }

    /**
     * Advice for logging service and repository
     *
     * @param proceedingJoinPoint join point.
     * @throws Throwable if an exception is thrown.
     */
    @Around("pointCutForJPARepository() || pointCutForServiceLayer()")
    public Object createAdviceForLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String logMessage = createLoggingRootMessage(proceedingJoinPoint);
        log.debug(logMessage);
        return proceedingJoinPoint.proceed();
    }

    /**
     * Creating Advice that will be executed if any exception occurs in the service.
     *
     * @param serviceException which took off in the service.
     */
    @AfterThrowing(value = "pointCutForServiceLayer()", throwing = "serviceException")
    public void createAdviceForLoggingServiceExceptions(Exception serviceException) {
        log.error(serviceException.getMessage());
    }

    /**
     * Сreate logging Root Message.
     * @param proceedingJoinPoint  proceeding Join Point.
     * @return message from specific layer.
     */
    private String createLoggingRootMessage(ProceedingJoinPoint proceedingJoinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ClassName ").append(proceedingJoinPoint.getSignature().getDeclaringTypeName());
        stringBuilder.append("MethodName ").append(proceedingJoinPoint.getSignature().getName().toUpperCase());
        stringBuilder.append("Parameter ").append(Arrays.toString(proceedingJoinPoint.getArgs()));
        return stringBuilder.toString();
    }
}
