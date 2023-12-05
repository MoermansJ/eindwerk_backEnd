package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.repository.h2.H2Repository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ScheduleService {
    private H2Repository h2Repository;


    public ScheduleService(H2Repository h2Repository) {
        this.h2Repository = h2Repository;
    }


    public void deleteRecordsOlderThanMinutes(long thresholdMinutes) {

        Instant thresholdTime = Instant.now().minusSeconds(thresholdMinutes * 60);
        h2Repository.deleteExpiredRecords(thresholdTime.getEpochSecond());
    }
}
