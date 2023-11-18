package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.dto.GameStateRequest;
import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.tetris.piece.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Arrays;

@Service
public class GameService {
    private int x = 3;
    private int y = 0;
    private static String[][] tileMap = createTileMap();
    private static ITetrisPiece currentPiece;

    public String[][] getGameState(GameStateRequest gameStateRequest) {
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userInput = gameStateRequest.getKey();
        String[][] newTileMap = tileMap;

        if (currentPiece == null) currentPiece = new PieceI();

        //playermove
        if (!userInput.equals("NO_KEY_PRESSED")) {
//            currentPiece = this.movePlayer(newTileMap, userInput);
            currentPiece.setShape(this.rotatePiece(currentPiece));
        }

        //computermove
//        if (computerMove) {
//            newTileMap = this.moveComputer(newTileMap);
//        }

        //checking & removing complete lines
//        newTileMap = this.clearCompleteLines(newTileMap);

        return currentPiece.getShape();
    }

    private String[][] rotatePiece(ITetrisPiece piece) {
        String[][] originalShape = piece.getShape();
        int rows = originalShape.length;
        int cols = originalShape[0].length;

        // Create a new 2D array for the rotated piece
        String[][] rotatedShape = new String[cols][rows];

        // Perform the rotation
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedShape[j][rows - 1 - i] = originalShape[i][j];
            }
        }

        return rotatedShape;
    }


    private ITetrisPiece movePlayer(String[][] tileMap, String key) {
        switch (key) {
            case "ARROWLEFT":
                if (this.x > 0) {
                    tileMap[x][y] = "_";    //clearing previous char
                    this.x -= 1;
                    tileMap[x][y] = "P";    //printing next char
                }
                break;
            case "ARROWRIGHT":
                if (this.x < tileMap.length - 1) {
                    tileMap[x][y] = "_";    //clearing previous char
                    this.x += 1;
                    tileMap[x][y] = "P";    //printing next char
                }
                break;
            case "ARROWDOWN":
                if (this.y < tileMap[x].length - 1) {
                    tileMap[x][y] = "_";    //clearing previous char
                    this.y += 1;
                    tileMap[x][y] = "P";    //printing next char
                }
                break;
            case "ARROWUP":
                //REPLACE THIS LOGIC WITH LOGIC TO ROTATE THE BLOCK
//                if (this.y > 0) {
//                    tileMap[x][y] = "_";    //clearing previous char
//                    this.y -= 1;
//                    tileMap[x][y] = "P";    //printing next char
//                }
        }

        return new PieceZ();
    }


    private String[][] moveComputer(String[][] tileMap) {
        if (!canMoveDownOneRow()) {
            //LOGIC TO STOP OR RESTART THE GAME
            x = 3;
            y = 0;
            tileMap[x][y] = "P";
            return tileMap;
        }

        //clearing previous char
        tileMap[x][y] = "_";

        //printing char one row down
        this.y++;
        tileMap[x][y] = "P";

        return tileMap;
    }


    private static String[][] createTileMap() {
        String[][] tilemap = new String[7][7];

        //filling with _
        for (String[] strings : tilemap) {
            Arrays.fill(strings, "_");
        }

        //player starting position
        int x = 3;
        int y = 0;
        tilemap[x][y] = "P";

        return tilemap;
    }

    private boolean canMoveDownOneRow() {
        int indexBelowCurrent = y + 1;

        //if bottom boundary is reached OR if obstructed
        if (indexBelowCurrent >= tileMap[x].length || !tileMap[x][indexBelowCurrent].equals("_")) {
            return false;
        }

        return true;
    }

    private String[][] clearCompleteLines(String[][] tileMap) {
        int rows = tileMap[0].length;
        int columns = tileMap.length;

        for (int y = 0; y < rows; y++) {
            if (isCompleteLine(tileMap, y, columns)) {
                for (int x = 0; x < columns; x++) {
                    tileMap[x][y] = "_";
                }
            }
        }

        return tileMap;
    }

    private boolean isCompleteLine(String[][] tileMap, int row, int columns) {
        for (int x = 0; x < columns; x++) {
            if (tileMap[x][row].equals("_")) {
                return false;
            }
        }
        return true;
    }

    public static void resetGrid() {
        tileMap = createTileMap();
    }
}


