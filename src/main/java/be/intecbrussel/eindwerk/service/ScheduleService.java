package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.model.HighScore;
import be.intecbrussel.eindwerk.repository.h2.H2Repository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
        h2Repository.deleteExpiredRecords(thresholdTime.getEpochSecond());
    }

    public void saveNewHighScores() {
        List<GameState> actualGameStatesWithUsers = this.fetchActualGameStatesWithUsers();
        List<HighScore> actualHighScores = deriveHighScoresFromGameStates(actualGameStatesWithUsers);
        actualHighScores.forEach(this::saveNewHighScoreIfBetterThanPrevious);
    }

    private List<GameState> fetchActualGameStatesWithUsers() {
        List<GameState> h2Gamestates = h2Repository.findAll();
        return h2Gamestates.stream()
                .filter(gameState -> !gameState.getUsername().trim().isEmpty())
                .collect(Collectors.toList());
    }

    private List<HighScore> deriveHighScoresFromGameStates(List<GameState> gameStates) {
        return gameStates.stream().map(gameState -> {
            int highScore = gameState.getCurrentPieceTileMap().getLinesCleared();
            return new HighScore(gameState.getUsername(), highScore);
        }).collect(Collectors.toList());
    }

    private void saveNewHighScoreIfBetterThanPrevious(HighScore newHighScore) {
        try {
            HighScore oldHighScore = this.highScoreService.findByUsername(newHighScore.getUsername());
            long oldScore = this.highScoreService.findByUsername(newHighScore.getUsername()).getScore();
            long newScore = newHighScore.getScore();

            if (!(newScore > oldScore))
                return;

            oldHighScore.setScore(newScore);
            highScoreService.saveHighScore(oldHighScore);
        } catch (InvalidCredentialsException invalidCredentialsException) {
            // If username doesn't occur in tb_highscore
            highScoreService.saveHighScore(newHighScore);
        }
    }
}
