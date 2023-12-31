package be.intecbrussel.eindwerk.repository.mysql;

import be.intecbrussel.eindwerk.model.HighScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {
    Optional<HighScore> findByUsername(String username);

    List<HighScore> findAllByOrderByScoreDesc();
}
