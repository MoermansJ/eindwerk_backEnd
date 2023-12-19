package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.repository.GameStateMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameStateMemoryService {
    @Autowired
    private GameStateMemoryRepository gameStateMemoryRepository;

    public GameState save(GameState gameState) {
        return this.gameStateMemoryRepository.save(gameState);
    }

    public Optional<GameState> getLatestGameStateBySessionId(String sessionId) {
        return gameStateMemoryRepository.findFirstBySessionIdOrderByTimeStampDesc(sessionId);
    }
}