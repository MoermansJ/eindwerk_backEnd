package be.intecbrussel.eindwerk.aspect.auth;

import be.intecbrussel.eindwerk.logger.FileLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

public class ValidateTokenAspect {
    private final FileLogger fileLogger;

    public ValidateTokenAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }

    @Before("execution(* be.intecbrussel.eindwerk.service.AuthService.validateToken(..))")
    public void beforeValidateTokenAttempt(JoinPoint joinPoint) {
        fileLogger.log("Attempting to validate token: " + (joinPoint.getArgs()[0]));
    }

    @After("execution(* be.intecbrussel.eindwerk.service.AuthService.validateToken(..))")
    public void afterValidateTokenServiceMethods(JoinPoint joinPoint) {
        fileLogger.log("Token validation attempt concluded: " + (joinPoint.getArgs()[0]));
    }

//    @AfterReturning(
//            pointcut = "execution(* be.intecbrussel.eindwerk.service.AuthService.validateToken(..))",
//            returning = "methodResult"
//    )
//    public void afterReturningValidateToken(JoinPoint joinPoint, boolean methodResult) {
//        String token = joinPoint.getArgs()[0];
//        if (methodResult)
//            fileLogger.log("Token successfully validated: " + token);
//        else
//            fileLogger.log("Token validation failed: " + token);
//    }

    @AfterThrowing(
            pointcut = "execution(* be.intecbrussel.eindwerk.service.AuthService.validateToken(..))",
            throwing = "exception"
    )
    public void afterValidateTokenThrowsException(JoinPoint joinPoint, Exception exception) {
        fileLogger.log("Token validation failed: " + joinPoint.getArgs()[0]);
        fileLogger.log("EXCEPTION: " + exception.getMessage());
        fileLogger.logException(exception);
    }
}
