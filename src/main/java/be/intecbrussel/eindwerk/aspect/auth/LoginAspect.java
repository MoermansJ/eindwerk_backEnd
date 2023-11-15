package be.intecbrussel.eindwerk.aspect.auth;

import be.intecbrussel.eindwerk.dto.AuthAttempt;
import be.intecbrussel.eindwerk.dto.LoginResponse;
import be.intecbrussel.eindwerk.logger.FileLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginAspect {
    private final FileLogger fileLogger;

    public LoginAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }

    @Before("execution(* be.intecbrussel.eindwerk.service.AuthService.login(..))")
    public void beforeLoginAttempt(JoinPoint joinPoint) {
        fileLogger.log("Attempting to log in: " + ((AuthAttempt) joinPoint.getArgs()[0]).getUsername());
    }

    @After("execution(* be.intecbrussel.eindwerk.service.AuthService.login(..))")
    public void afterRegisterServiceMethods(JoinPoint joinPoint) {
        fileLogger.log("Log in attempt concluded: " + ((AuthAttempt) joinPoint.getArgs()[0]).getUsername());
    }

    @AfterReturning(
            pointcut = "execution(* be.intecbrussel.eindwerk.service.AuthService.login(..))",
            returning = "methodResult"
    )
    public void afterReturningLoginResponse(JoinPoint joinPoint, LoginResponse methodResult) {
        if (methodResult != null)
            fileLogger.log("Log in attempt successful: " + ((AuthAttempt) joinPoint.getArgs()[0]).getUsername());
        else
            fileLogger.log("Log in attempt failed: No user found");
    }

    @AfterThrowing(
            pointcut = "execution(* be.intecbrussel.eindwerk.service.AuthService.login(..))",
            throwing = "exception"
    )
    public void afterLoginThrowsException(JoinPoint joinPoint, Exception exception) {
        fileLogger.log("Log in attempt failed: " + ((AuthAttempt) joinPoint.getArgs()[0]).getUsername());
        fileLogger.log("EXCEPTION: " + exception.getMessage());
        fileLogger.logException(exception);
    }
}
