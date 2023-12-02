package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.config.MysqlDataSourceConfig;
import be.intecbrussel.eindwerk.games.tetris.dto.GameStateRequest;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import be.intecbrussel.eindwerk.games.tetris.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameStateMemoryService gameStateMemoryService;
    @Autowired
    private TileMapService tileMapService;
    @Autowired
    private MovementService movementService;
    @Autowired
    private TetrisPieceService tetrisPieceService;
    @Autowired
    private MysqlDataSourceConfig dynamicDataSourceConfig;


    public GameState getGameState(GameStateRequest gameStateRequest) {
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userInput = gameStateRequest.getKey();
        String sessionId = gameStateRequest.getSessionId();

        Optional<GameState> oDbGameState = gameStateMemoryService.getLatestGameStateBySessionId(sessionId);

        if (oDbGameState.isEmpty()) {
            return this.getDefaultGamestate(gameStateRequest.getSessionId());
        }

        GameState gameState = oDbGameState.get();
        tileMapService.unpaintTetrisPiece(gameState);

        if (computerMove) {
            movementService.doComputerMove(gameState);
        }

        if (!userInput.equals("NO_KEY")) {
            movementService.doUserMove(gameState, gameStateRequest.getKey());
        }

        tileMapService.paintTetrisPiece(gameState);


        gameState.setTimeStamp(System.currentTimeMillis());

        gameStateMemoryService.save(gameState);

        return gameState;
    }

    private GameState getDefaultGamestate(String sessionId) {
        GameState gameState = new GameState();
        int width = 10, height = 20;

        TetrisPiece tetrisPiece = tetrisPieceService.getNextTetrisPiece();
        List<Tile> emptyTiles = tileMapService.createEmptyTileMap(width, height);
        TileMap defaultTileMap = new TileMap(width, height, emptyTiles);

        gameState.setTileMap(defaultTileMap);
        gameState.setCurrentPiece(tetrisPiece);
        gameState.setSessionId(sessionId);
        gameState.setTimeStamp(System.currentTimeMillis());

        tileMapService.positionNewTetrisPiece(gameState);

        return gameStateMemoryService.save(gameState);
    }
}