package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.dto.GameStateRequest;
import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.tetris.GameState;
import be.intecbrussel.eindwerk.model.tetris.Tile;
import be.intecbrussel.eindwerk.model.tetris.TileMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

@Service
public class GameService {
    //properties
    private GameState gameState;

    private GameStateMemoryService gameStateMemoryService;


    //constructors
    public GameService(GameStateMemoryService gameStateMemoryService) {
        this.gameStateMemoryService = gameStateMemoryService;

        if (gameState == null) {
            this.gameState = new GameState();
        }
    }


    //custom methods
    public GameState getGameState(GameStateRequest gameStateRequest) {
        //game loop logic
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userInput = gameStateRequest.getKey();

        Optional<GameState> oDbGameState = gameStateMemoryService.getLatestGameState(); //find another way to check if there's a record so i don't have to use silly optionals

        if (oDbGameState.isPresent())
            this.gameState = oDbGameState.get();

        //playermove
        if (!userInput.equals("NO_KEY"))
            gameState.movePlayer(gameStateRequest.getKey());

        //computermove
        if (computerMove)
            gameState.moveComputer();

        gameState.setTimeStamp(LocalDateTime.now());
        gameStateMemoryService.save(gameState);

        return this.gameState;
    }
}