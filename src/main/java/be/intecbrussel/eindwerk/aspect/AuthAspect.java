package be.intecbrussel.eindwerk.aspect;

import be.intecbrussel.eindwerk.dto.AuthAttempt;
import be.intecbrussel.eindwerk.logger.FileLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {
    @Autowired
    private FileLogger fileLogger;


    //login
    @Around("execution(* be.intecbrussel.eindwerk.service.AuthService.login(..))")
    public Object aroundLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Attempting login", "Login complete", "Login failed");
    }


    //register
    @Around("execution(* be.intecbrussel.eindwerk.service.AuthService.register(..))")
    public Object aroundRegister(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Attempting registration", "Registration complete", "Registration failed");
    }


    //validateToken
    @Around("execution(* be.intecbrussel.eindwerk.service.AuthService.validateToken(..))")
    public Object aroundValidateToken(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Attempting token validation", "Token validation complete", "Token validation failed");
    }


    //log method
    private <T> Object logMethodExecution(ProceedingJoinPoint joinPoint, String beforeMessage, String afterMessage, String errorMessage) throws Throwable {
        try {
            T methodArgument = ((T) joinPoint.getArgs()[0]);

            fileLogger.log(beforeMessage + ": " + methodArgument);

            Object returnedObject = joinPoint.proceed(); // execute method

            // After / AfterReturning with returnedObject
            fileLogger.log(afterMessage + ": " + methodArgument);
            return returnedObject; // returning to the regular flow

        } catch (Throwable e) {
            // AfterThrowing
            fileLogger.log(errorMessage + ": " + e.getMessage());
            fileLogger.logException(e);
            throw e; // returning to the regular flow
        }
    }
}

