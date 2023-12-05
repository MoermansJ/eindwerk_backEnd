package be.intecbrussel.eindwerk.aspect;

import be.intecbrussel.eindwerk.logger.FileLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ScheduleAspect {
    private FileLogger fileLogger;


    public ScheduleAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }


    //getGameState
    @Around("execution(* be.intecbrussel.eindwerk.service.DatabaseService.cleanExpiredRecordsOlderThanFifteenMinutes()))")
    public void aroundGetGameState(ProceedingJoinPoint joinPoint) throws Throwable {
        logMethodExecution(joinPoint,
                "Attempting to clean H2 database.",
                "H2 database cleaned of records older than 15 minutes.",
                "H2 database cleaning failed.");
    }

    //log method
    private void logMethodExecution(ProceedingJoinPoint joinPoint, String beforeMessage, String afterMessage, String errorMessage) throws Throwable {
        fileLogger.log(beforeMessage);
        try {
            fileLogger.log(afterMessage);
        } catch (Throwable e) {
            fileLogger.log(errorMessage + ": " + e.getMessage());
            fileLogger.logException(e);
            throw e;
        }
    }
}
