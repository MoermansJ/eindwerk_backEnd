package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.HighScore;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.mysql.HighScoreRepository;
import be.intecbrussel.eindwerk.repository.mysql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HighScoreService {
    private HighScoreRepository highScoreRepository;
    private UserRepository userRepository;


    public HighScoreService(HighScoreRepository highScoreRepository, UserRepository userRepository) {
        this.highScoreRepository = highScoreRepository;
        this.userRepository = userRepository;
    }


    public HighScore saveHighScore(HighScore highScore) {
        User user = highScore.getUser();

        Optional<HighScore> oDbHighScore = highScoreRepository.findHighScoreByUser(user);

        if (oDbHighScore.isEmpty()) {
            return highScoreRepository.save(highScore);
        }

        HighScore dbHighscore = oDbHighScore.get();

        if (highScore.getScore() <= dbHighscore.getScore()) {
            return dbHighscore;
        }

        return this.highScoreRepository.save(highScore);
    }

    private HighScore getHighScoreByUser(User user) {
        Optional<HighScore> dbHighScore = this.highScoreRepository.findHighScoreByUser(user);

        if (dbHighScore.isEmpty())
            throw new InvalidCredentialsException("User " + user.getUsername() + " does not have a highscore.");

        return dbHighScore.get();
    }
}
