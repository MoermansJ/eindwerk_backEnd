package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.Direction;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.dto.GameStateRequest;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameStateService {
    private GameStateMemoryService gameStateMemoryService;
    private TileMapService tileMapService;
    private MovementService movementService;
    private TetrisPieceGenerator tetrisPieceGenerator;

    public GameStateService(GameStateMemoryService gameStateMemoryService,
                            TileMapService tileMapService,
                            MovementService movementService,
                            TetrisPieceGenerator tetrisPieceGenerator) {
        this.gameStateMemoryService = gameStateMemoryService;
        this.tileMapService = tileMapService;
        this.movementService = movementService;
        this.tetrisPieceGenerator = tetrisPieceGenerator;
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
        String seed = gameStateRequest.getSeed();
        int width = 10, height = 20;

        List<Integer> seededGenerationPattern = tetrisPieceGenerator.getGenerationPattern(seed, 100);
        TetrisPiece firstTetrisPiece = tetrisPieceGenerator.getNextTetrisPiece(seededGenerationPattern.get(0));
        List<Tile> emptyTiles = tileMapService.createEmptyTileMap(width, height);
        TileMap defaultTileMap = new TileMap(width, height, emptyTiles);

        int firstSeedElement = seededGenerationPattern.remove(0);
        seededGenerationPattern.add(firstSeedElement);
        gameState.setSeededGenerationPattern(seededGenerationPattern);

        gameState.setTileMap(defaultTileMap);
        gameState.setCurrentPiece(firstTetrisPiece);
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
