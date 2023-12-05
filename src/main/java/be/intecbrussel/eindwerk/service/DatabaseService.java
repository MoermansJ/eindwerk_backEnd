package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.repository.h2.H2Repository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DatabaseService {
    private H2Repository h2Repository;


    public DatabaseService(H2Repository h2Repository) {
        this.h2Repository = h2Repository;
    }

    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void cleanExpiredRecordsOlderThanFifteenMinutes() {
        // TO DO: Add this method call to Aspect for logging.
        Instant minutesAgo = Instant.now().minusSeconds(15 * 60);
        h2Repository.deleteExpiredRecords(minutesAgo.getEpochSecond());
    }
}
