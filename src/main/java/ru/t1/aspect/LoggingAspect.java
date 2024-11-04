package ru.t1.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Log log = LogFactory.getLog(LoggingAspect.class);

    @Before("@annotation(ru.t1.annotation.LogBefore)")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        if (args != null && args.length > 0) {
            log.debug(String.format(
                    "Method %s.%s called with arguments: %s",
                    className,
                    methodName,
                    Arrays.toString(args))
            );
        } else {
            log.debug(String.format(
                    "Method %s.%s called without arguments",
                    className,
                    methodName)
            );
        }
    }

    @Around("@annotation(ru.t1.annotation.LogExecution)")
    public Object logExecution(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        long startTime = System.currentTimeMillis();

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.warn(String.format(
                    "Method %s.%s thrown exception: %s",
                    className,
                    methodName,
                    e.getMessage())
            );
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.debug(String.format(
                "Method %s.%s executed time: %d ms",
                className,
                methodName,
                duration)
        );

        return result;
    }

    @AfterThrowing(
            pointcut = "@annotation(ru.t1.annotation.LogException)",
            throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        log.error(String.format(
                "Method %s.%s thrown exception: %s",
                className,
                methodName,
                ex.getMessage())
        );
    }

    @AfterReturning(
            pointcut = "@annotation(ru.t1.annotation.LogResult)",
            returning = "result"
    )
    public void logResult(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        if (result != null) {
            log.debug(String.format(
                    "Method %s.%s returned value: %s",
                    className,
                    methodName,
                    result)
            );
        }
        else {
            log.debug(String.format(
                    "Method %s.%s exiting without returning value (void method)",
                    className,
                    methodName)
            );
        }
    }

}
