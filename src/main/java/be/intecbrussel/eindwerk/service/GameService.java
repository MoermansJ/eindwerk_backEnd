package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.games.tetris.dto.GameStateRequest;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import be.intecbrussel.eindwerk.games.tetris.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    // Properties
    @Autowired
    private GameStateMemoryService gameStateMemoryService;
    @Autowired
    private TileMapService tileMapService;
    @Autowired
    private MovementService movementService;
    @Autowired
    private TetrisPieceService tetrisPieceService;


    // Custom methods
    public GameState getGameState(GameStateRequest gameStateRequest) {
        // Game loop logic
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userInput = gameStateRequest.getKey();
        String sessionId = gameStateRequest.getSessionId();

        Optional<GameState> oDbGameState = gameStateMemoryService.getLatestGameStateBySessionId(sessionId);

        if (oDbGameState.isEmpty()) {
            return this.getDefaultGamestate(gameStateRequest.getSessionId());
        }

        GameState gameState = oDbGameState.get();

        // Player input
        if (!userInput.equals("NO_KEY"))
            movementService.movePlayer(gameState, gameStateRequest.getKey());

        // Computer
        if (computerMove)
            movementService.moveComputer(gameState);

        gameStateMemoryService.save(gameState);

        return gameState;
    }

    private GameState getDefaultGamestate(String sessionId) {
        GameState gameState = new GameState();
        gameState.setSessionId(sessionId);

        List<Tile> emptyTileMap = tileMapService.createEmptyTileMap(10, 20);
        gameState.getTileMap().setTiles(emptyTileMap);

        TetrisPiece tetrisPiece = tetrisPieceService.getNextTetrisPiece();
        gameState.setCurrentPiece(tetrisPiece);

        tileMapService.paintTetrisPiece(gameState);

        gameStateMemoryService.save(gameState);

        return gameState;
    }
}