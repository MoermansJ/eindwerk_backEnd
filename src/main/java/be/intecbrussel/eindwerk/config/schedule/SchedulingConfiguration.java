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
    private ScheduleService scheduleService;

    public SchedulingConfiguration(ScheduleService databaseService) {
        this.scheduleService = databaseService;
    }

    @Scheduled(fixedRate = 60000)
    private void cleanH2Database() {
        scheduleService.saveNewHighScores();
        scheduleService.deleteRecordsOlderThanMinutes(1);
    }
}
