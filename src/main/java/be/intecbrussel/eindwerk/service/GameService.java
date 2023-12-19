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
    private GameStateService gameStateService;


    public GameService(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }


    public GameState getGameState(GameStateRequest gameStateRequest) {
        return gameStateService.getGameState(gameStateRequest);
    }
}