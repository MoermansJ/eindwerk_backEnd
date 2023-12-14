package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.games.tetris.model.dto.GameStateRequest;
import be.intecbrussel.eindwerk.games.tetris.model.Direction;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import be.intecbrussel.eindwerk.games.tetris.service.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
    private GameStateMemoryService gameStateMemoryService;
    private TileMapService tileMapService;
    private MovementService movementService;
    private TetrisPieceService tetrisPieceService;


    public GameService(GameStateMemoryService gameStateMemoryService,
                       TileMapService tileMapService,
                       MovementService movementService,
                       TetrisPieceService tetrisPieceService) {
        this.gameStateMemoryService = gameStateMemoryService;
        this.tileMapService = tileMapService;
        this.movementService = movementService;
        this.tetrisPieceService = tetrisPieceService;
    }


    public GameState getGameState(GameStateRequest gameStateRequest) {
        String sessionId = gameStateRequest.getSessionId();
        Optional<GameState> oDbGameState = gameStateMemoryService.getLatestGameStateBySessionId(sessionId);

        if (oDbGameState.isEmpty()) {
            return this.getDefaultGamestate(gameStateRequest);
        }

        return this.updateGameState(gameStateRequest, oDbGameState.get());
    }

    private GameState getDefaultGamestate(GameStateRequest gameStateRequest) {
        GameState gameState = new GameState();
        String username = gameStateRequest.getUsername();
        String sessionId = gameStateRequest.getSessionId();
        int width = 10, height = 20;

        TetrisPiece tetrisPiece = tetrisPieceService.getNextTetrisPiece();
        List<Tile> emptyTiles = tileMapService.createEmptyTileMap(width, height);
        TileMap defaultTileMap = new TileMap(width, height, emptyTiles);

        gameState.setTileMap(defaultTileMap);
        gameState.setCurrentPiece(tetrisPiece);
        gameState.setSessionId(sessionId);
        gameState.setTimeStamp(System.currentTimeMillis());
        gameState.setUsername(username);

        tileMapService.positionNewTetrisPiece(gameState);

        return gameStateMemoryService.save(gameState);
    }

    private GameState updateGameState(GameStateRequest gameStateRequest, GameState gameState) {
        List<Direction> movementBuffer = gameStateRequest.getMovementBuffer().stream()
                .map(movement -> movementService.getDirectionFromString(movement))
                .collect(Collectors.toList());

        movementBuffer.forEach(movement -> movementService.executeMovement(gameState, movement));

        gameState.setTimeStamp(System.currentTimeMillis());
        return gameStateMemoryService.save(gameState);
    }
}