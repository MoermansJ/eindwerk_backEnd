package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.games.tetris.dto.GameStateRequest;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import be.intecbrussel.eindwerk.games.tetris.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
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

        tileMapService.unpaintTetrisPiece(gameState);

        // Player input
        if (!userInput.equals("NO_KEY")) {
            movementService.movePlayer(gameState, gameStateRequest.getKey());
        }

        tileMapService.paintTetrisPiece(gameState);

        // Computer
        if (computerMove) {
            movementService.doComputerMove(gameState);
        }

        gameState.setTimeStamp(System.currentTimeMillis());
        gameStateMemoryService.save(gameState);

        return gameState;
    }

    private GameState getDefaultGamestate(String sessionId) {
        GameState gameState = new GameState();
        gameState.setSessionId(sessionId);

        // SET BACK TO WIDTH 10 HEIGHT 20 AFTER TESTING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        int width = 6, height = 10;
        List<Tile> emptyTiles = tileMapService.createEmptyTileMap(width, height);
        TileMap defaultTileMap = new TileMap(width, height, emptyTiles);
        gameState.setTileMap(defaultTileMap);

        TetrisPiece tetrisPiece = tetrisPieceService.getNextTetrisPiece();
        gameState.setCurrentPiece(tetrisPiece);

        tileMapService.positionNewTetrisPiece(gameState);
        tileMapService.paintTetrisPiece(gameState);

        gameState.setTimeStamp(System.currentTimeMillis());
        return gameStateMemoryService.save(gameState);
    }

}