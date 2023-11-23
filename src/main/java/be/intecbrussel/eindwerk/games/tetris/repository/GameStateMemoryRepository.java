package be.intecbrussel.eindwerk.games.tetris.repository;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameStateMemoryRepository extends JpaRepository<GameState, Long> {
    @Query("SELECT g FROM GameState g WHERE g.sessionId = :sessionId ORDER BY g.timeStamp DESC")
    Optional<GameState> findMostRecentGameStateBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT g FROM GameState g ORDER BY g.timeStamp DESC")
    Optional<GameState> findMostRecentGameState();
}
