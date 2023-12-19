package be.intecbrussel.eindwerk.aspect;

import be.intecbrussel.eindwerk.util.logger.FileLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ScheduleAspect {
    private static String classPrefix = "SCHEDULE - ";
    private FileLogger fileLogger;


    public ScheduleAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }


    //getGameState
    @Around("execution(* be.intecbrussel.eindwerk.service.ScheduleService.deleteRecordsOlderThanMinutes(..)))")
    public void aroundGetGameState(ProceedingJoinPoint joinPoint) throws Throwable {
        logMethodExecution(joinPoint,
                classPrefix + "Attempting to clean H2 database.",
                classPrefix + "H2 database cleaned.",
                classPrefix + "H2 database cleaning failed.");
    }

    //log method
    private void logMethodExecution(ProceedingJoinPoint joinPoint, String beforeMessage, String afterMessage, String errorMessage) throws Throwable {
        fileLogger.log(beforeMessage);
        try {
            fileLogger.log(afterMessage + " Deleted records older than " + joinPoint.getArgs()[0] + " minutes.");
            joinPoint.proceed();
        } catch (Throwable e) {
            fileLogger.log(errorMessage + ": " + e.getMessage());
            fileLogger.logException(e);
            throw e;
        }
    }
}
