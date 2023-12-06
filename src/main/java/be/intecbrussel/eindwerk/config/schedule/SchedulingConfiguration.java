package be.intecbrussel.eindwerk.config.schedule;

import be.intecbrussel.eindwerk.service.ScheduleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
@Component
public class SchedulingConfiguration {
    private ScheduleService databaseService;


    public SchedulingConfiguration(ScheduleService databaseService) {
        this.databaseService = databaseService;
    }


    //    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    @Scheduled(fixedRate = 5000) // 15 minutes in milliseconds
    private void cleanH2Database() {
//        databaseService.deleteRecordsOlderThanMinutes(15);
        databaseService.deleteRecordsOlderThanMinutes(1);
    }
}
