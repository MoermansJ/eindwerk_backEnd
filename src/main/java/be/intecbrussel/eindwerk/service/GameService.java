package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.games.tetris.dto.GameStateRequest;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import be.intecbrussel.eindwerk.games.tetris.service.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userKeyInput = gameStateRequest.getKeyPressed();

        tileMapService.unpaintTetrisPiece(gameState);

        if (computerMove) {
            movementService.doComputerMove(gameState);
        }

        if (!userKeyInput.equals("NO_KEY")) {
            movementService.doUserMove(gameState, gameStateRequest.getKeyPressed());
        }

        gameState.setTimeStamp(System.currentTimeMillis());
        tileMapService.paintTetrisPiece(gameState);

        return gameStateMemoryService.save(gameState);
    }
}