package be.intecbrussel.eindwerk.repository;

import be.intecbrussel.eindwerk.model.tetris.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameStateMemoryRepository extends JpaRepository<GameState, Long> {
    @Query("SELECT g FROM GameState g ORDER BY g.timeStamp DESC")
    Optional<GameState> findMostRecentGameState();
}
