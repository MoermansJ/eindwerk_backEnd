package be.intecbrussel.eindwerk.aspect.auth;

import be.intecbrussel.eindwerk.dto.AuthAttempt;
import be.intecbrussel.eindwerk.logger.FileLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.stereotype.Component;

@Component
@Aspect
public class RegisterAspect {
    private final FileLogger fileLogger;

    public RegisterAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }

    @Around("execution(* be.intecbrussel.eindwerk.service.AuthService.register(..))")
    public void aroundRegister(ProceedingJoinPoint joinPoint) {
        // @Before
        String username = ((AuthAttempt) joinPoint.getArgs()[0]).getUsername();
        fileLogger.log("Attempting registration: " + username);
        try {
            Object returnedObject = joinPoint.proceed(); // execute method
            // @After / @AfterReturning with returnedObject
            fileLogger.log("Registration complete: " + username);
        } catch (Throwable e) {
            // @AfterThrowing
            fileLogger.log("Registration failed: " + username);
            fileLogger.log("EXCEPTION: " + e.getMessage());
            fileLogger.logException(e);
        }
    }
}