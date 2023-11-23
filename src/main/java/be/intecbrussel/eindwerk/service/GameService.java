package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.games.tetris.dto.GameStateRequest;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import be.intecbrussel.eindwerk.games.tetris.service.GameStateMemoryService;
import be.intecbrussel.eindwerk.games.tetris.service.GameStateService;
import be.intecbrussel.eindwerk.games.tetris.service.TileMapService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    //properties
    private GameStateMemoryService gameStateMemoryService;
    private GameStateService gameStateService;
    private TileMapService tileMapService;


    //constructors
    public GameService(GameStateService gameStateService, GameStateMemoryService gameStateMemoryService, TileMapService tileMapService) {
        this.gameStateMemoryService = gameStateMemoryService;
        this.gameStateService = gameStateService;
        this.tileMapService = tileMapService;
    }


    //custom methods
    public GameState getGameState(GameStateRequest gameStateRequest, HttpSession session) {
        // Game loop logic
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userInput = gameStateRequest.getKey();

//        Optional<GameState> oDbGameState = gameStateMemoryService.getLatestGameStateBySessionId(session.getId()); //find another way to check if there's a record so i don't have to use silly optionals
        Optional<GameState> oDbGameState = gameStateMemoryService.getMostRecentGameState();

        // Initialising game -> Make this into a seperate method
        if (oDbGameState.isEmpty()) {
            GameState gameState = new GameState();
            gameState.setSessionId(session.getId());

            List<Tile> emptyTileMap = tileMapService.createEmptyTileMap(10, 20);
            gameState.getTileMap().setTiles(emptyTileMap);

            TetrisPiece tetrisPiece = gameStateService.getNextTetrisPiece();
            gameState.setCurrentPiece(tetrisPiece);

            tileMapService.paintTetrisPiece(gameState);

            gameStateMemoryService.save(gameState);

            return gameState;
        }

        GameState gameState = oDbGameState.get();

        // Playermove
        if (!userInput.equals("NO_KEY"))
            gameStateService.movePlayer(gameState, gameStateRequest.getKey());

        // Computermove
        if (computerMove)
            gameStateService.moveComputer(gameState);

        gameStateMemoryService.save(gameState);

        return gameState;
    }
}