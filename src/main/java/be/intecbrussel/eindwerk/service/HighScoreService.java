package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.HighScore;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.mysql.HighScoreRepository;
import be.intecbrussel.eindwerk.repository.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HighScoreService {
    private HighScoreRepository highScoreRepository;


    public HighScoreService(HighScoreRepository highScoreRepository) {
        this.highScoreRepository = highScoreRepository;
    }


    public HighScore saveHighScore(HighScore highScore) {
        String username = highScore.getUsername();

        Optional<HighScore> oDbHighScore = highScoreRepository.findByUsername(username);

        if (oDbHighScore.isEmpty()) {
            return highScoreRepository.save(highScore);
        }

        HighScore dbHighscore = oDbHighScore.get();

        if (highScore.getScore() <= dbHighscore.getScore()) {
            return dbHighscore;
        }

        return this.highScoreRepository.save(highScore);
    }

    public HighScore findByUsername(String username) {
        return this.highScoreRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Username " + username + " does not have a highscore."));
    }

    public List<HighScore> findAllHighScores() {
        return this.highScoreRepository.findAllByOrderByScoreDesc();
    }
}
