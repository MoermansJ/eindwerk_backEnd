package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.HighScore;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.mysql.HighScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HighScoreService {
    @Autowired
    private HighScoreRepository highScoreRepository;

    public HighScore saveHighScore(HighScore highScore) {
        return this.highScoreRepository.save(highScore);
    }

    public HighScore getHighScoreByUser(User user) {
        Optional<HighScore> dbHighScore = this.highScoreRepository.findHighScoreByUser(user);

        if (dbHighScore.isEmpty())
            throw new InvalidCredentialsException("User " + user.getUsername() + " does not have a highscore.");

        return dbHighScore.get();
    }
}
