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
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Arrays;

@Service
public class GameService {
    //properties
    private GameState gameState;

    private String[][] matrix;
    private static boolean firstGame = true;


    //constructor
    public GameService() {
        if (gameState == null) {
            this.gameState = new GameState();
        }
    }


    //custom methods
    public GameState getGameState(GameStateRequest gameStateRequest) {
        //game loop logic
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userInput = gameStateRequest.getKey();

        //playermove
        if (!userInput.equals("NO_KEY")) {
            gameState.movePlayer(gameStateRequest.getKey());
        }

        return this.gameState;
    }
}