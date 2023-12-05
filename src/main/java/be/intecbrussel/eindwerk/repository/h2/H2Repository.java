package be.intecbrussel.eindwerk.repository.h2;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface H2Repository extends JpaRepository<GameState, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM GameState g WHERE g.timeStamp < :expirationTimeStamp")
    int deleteExpiredRecords(@Param("expirationTimeStamp") Long expirationTimeStamp);
}
