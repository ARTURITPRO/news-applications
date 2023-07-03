package ru.clevertec.logging.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

/**
 * <p>An aspect class that adds functionality for logging the controller layer.
 * @author Artur Malashkov
 * @since 17
 */
@Slf4j
@Aspect
public class ControllerLogAspect {

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    private void pointCutForController() {
    }

    /**
     * Advice for logging controller.
     * @param proceedingJoinPoint join point.
     * @throws Throwable if an exception is thrown.
     */
    @Around("pointCutForController()")
    public Object createAdviceForLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String logMessage = createFirstMessage(proceedingJoinPoint);
        log.info(logMessage);

        Object proceed = proceedingJoinPoint.proceed();

        String generatedMessageFromController = generatedMessageFromController(proceed);
        log.info(generatedMessageFromController);
        return proceed;
    }


    /**
     * Advice for logging controller.
     * @param proceedingJoinPoint join point.
     * @return  the first part of the message from the controller.
     */
    private String createFirstMessage(ProceedingJoinPoint proceedingJoinPoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ClassName").append(proceedingJoinPoint.getSignature().getDeclaringTypeName());
        stringBuilder.append("MethodName").append(proceedingJoinPoint.getSignature().getName().toUpperCase());
        stringBuilder.append("Parameter").append(Arrays.toString(proceedingJoinPoint.getArgs()));
   
        return stringBuilder.toString();
    }

    /**
     * Ggenerated Message from Controller.
     * @param proceedObject proceed Object.
     * @return  the message from the controller.
     */
    private String generatedMessageFromController(Object proceedObject) {
        StringBuilder builder = new StringBuilder();
        ResponseEntity responseEntity = (ResponseEntity) proceedObject;
        builder.append(" Result Body:  ").append(responseEntity.getBody());
        builder.append(" Response status:  ").append(responseEntity.getStatusCode());
        return builder.toString();
    }

}
