package be.intecbrussel.eindwerk.aspect;

import be.intecbrussel.eindwerk.logger.FileLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GameAspect {
    private FileLogger fileLogger;


    public GameAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }


    //getGameState
    @Around("execution(* be.intecbrussel.eindwerk.service.GameService.getGameState(..))")
    public Object aroundGetGameState(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Attempting getGameState", "getGameState complete", "getGameState failed");
    }

    //log method
    private <T> Object logMethodExecution(ProceedingJoinPoint joinPoint, String beforeMessage, String afterMessage, String errorMessage) throws Throwable {
        try {
            T methodArgument = ((T) joinPoint.getArgs()[0]);

            fileLogger.log(beforeMessage + ": " + methodArgument);

            Object returnedObject = joinPoint.proceed(); // execute method

            // After / AfterReturning with returnedObject
            fileLogger.log(afterMessage + ": " + methodArgument);
            return returnedObject; // returning to regular flow

        } catch (Throwable e) {
            // AfterThrowing
            fileLogger.log(errorMessage + ": " + e.getMessage());
            fileLogger.logException(e);
            throw e; // returning to regular flow
        }
    }
}
