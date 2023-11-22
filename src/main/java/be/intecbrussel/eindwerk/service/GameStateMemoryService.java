package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.model.tetris.GameState;
import be.intecbrussel.eindwerk.repository.GameStateMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GameStateMemoryService {
    @Autowired
    private GameStateMemoryRepository gameStateMemoryRepository;

    public void save(GameState gameState) {
        this.gameStateMemoryRepository.save(gameState);
    }

    public Optional<GameState> getLatestGameState() {
        return gameStateMemoryRepository.findMostRecentGameState();
    }
}
