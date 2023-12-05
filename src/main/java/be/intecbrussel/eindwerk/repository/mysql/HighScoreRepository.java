package be.intecbrussel.eindwerk.repository.mysql;

import be.intecbrussel.eindwerk.model.HighScore;
import be.intecbrussel.eindwerk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {
    Optional<HighScore> findHighScoreByUser(User user);
}
