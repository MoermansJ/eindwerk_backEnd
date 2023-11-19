package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.dto.GameStateRequest;
import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.tetris.piece.*;
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
    private static int currentPieceTopLeftX;
    private static int currentPieceTopLeftY;
    private static TetrisPiece piece = new PieceZ();

    private static String[][] tileMap = createEmptyTileMap(); //logic to insert new piece is inside this method
    private static boolean firstGame = true;

    private enum Direction {
        LEFT, RIGHT, DOWN, UP
    }

    public String[][] getGameState(GameStateRequest gameStateRequest) {
        if (firstGame) {
            insertPiece(0, 0);
            firstGame = false;
        }

//game loop logic
        Boolean computerMove = gameStateRequest.getComputerMove();
        String userInput = gameStateRequest.getKey();
        String[][] newTileMap = tileMap;

        //playermove
        if (!userInput.equals("NO_KEY")) {
            movePlayer(gameStateRequest.getKey());
//            newTileMap = this.clearPiece(newTileMap, currentPiece.getShape());
//            newTileMap = this.movePlayer(newTileMap, userInput);
        }


        return newTileMap;
    }

    public static void insertPiece(int x, int y) {
        int pieceRows = piece.getShape().length;
        int pieceCols = piece.getShape()[0].length;

        for (int i = 0; i < pieceRows; i++) {
            for (int j = 0; j < pieceCols; j++) {
                tileMap[x + i][y + j] = piece.getShape()[i][j];
            }
        }
    }

    private void movePlayer(String key) {
        switch (key) {
            case "ARROWLEFT":
                movePiece(Direction.LEFT);
                break;
            case "ARROWRIGHT":
                movePiece(Direction.RIGHT);
                break;
            case "ARROWDOWN":
                movePiece(Direction.DOWN);
                break;
            case "ARROWUP":
                piece.rotateShape();
                movePiece(Direction.UP);
                break;
        }
    }

    public static void movePiece(Direction direction) {
        clearPiece();
        printPiece(direction);
    }

    private static void clearPiece() {
        for (Point point : piece.getPoints()) {
            int row = point.y;
            int col = point.x;

            if (row >= 0 && row < tileMap.length && col >= 0 && col < tileMap[0].length) {
                tileMap[row][col] = "_";
            }
        }
    }

    private static void printPiece(Direction direction) {
        // Calculate the new position after movement
        int xOffset = (direction == Direction.RIGHT) ? 1 : (direction == Direction.LEFT) ? -1 : 0;
        int yOffset = (direction == Direction.DOWN) ? 1 : 0;

        // Update the coordinates of the TetrisPiece points
        List<Point> updatedPoints = new ArrayList<>();
        for (Point point : piece.getPoints()) {
            updatedPoints.add(new Point(point.x + xOffset, point.y + yOffset));
        }
        piece.setPoints(updatedPoints);

        //same logic as in clearPiece
        for (Point point : piece.getPoints()) {
            int row = point.y;
            int col = point.x;

            if (row >= 0 && row < tileMap.length && col >= 0 && col < tileMap[0].length) {
                tileMap[row][col] = "P";
            }
        }
    }

    private static String[][] createEmptyTileMap() {
        String[][] tilemap = new String[7][7];

        //filling with _
        for (String[] strings : tilemap) {
            Arrays.fill(strings, "_");
        }

        return tilemap;
    }

    public static void resetGrid() {
        tileMap = createEmptyTileMap();
        firstGame = true;
        currentPieceTopLeftX = 0;
        currentPieceTopLeftY = 0;
    }
}


