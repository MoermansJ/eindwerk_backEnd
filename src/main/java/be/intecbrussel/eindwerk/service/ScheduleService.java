package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.model.HighScore;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.h2.H2Repository;
import be.intecbrussel.eindwerk.repository.mysql.HighScoreRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private H2Repository h2Repository;
    private HighScoreService highScoreService;


    public ScheduleService(H2Repository h2Repository, HighScoreService highScoreService) {
        this.h2Repository = h2Repository;
        this.highScoreService = highScoreService;
    }


    public void deleteRecordsOlderThanMinutes(long thresholdMinutes) {
        Instant thresholdTime = Instant.now().minusSeconds(thresholdMinutes * 60);
        List<GameState> h2Gamestates = h2Repository.findAll();

        h2Gamestates.stream()
                .filter(gameState -> gameState.getUserId() == null)
                .forEach(gameState -> {
                    highScoreService.saveHighScore(new HighScore(999L, 999L));
                });

        h2Repository.deleteExpiredRecords(thresholdTime.getEpochSecond());
    }
}
