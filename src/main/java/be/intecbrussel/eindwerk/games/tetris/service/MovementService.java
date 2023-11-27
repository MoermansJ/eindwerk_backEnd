package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.Direction;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.*;

@Service
public class MovementService {
    // Properties
    @Autowired
    private TetrisPieceService tetrisPieceService;
    @Autowired
    private TileMapService tileMapService;


    // Custom methods
    public GameState movePlayer(GameState gameState, String key) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();

        switch (key) {
            case "ARROWLEFT": {
                List<Point> updatedPoints = this.movePoints(currentPiece, Direction.LEFT);

                if (isObstructedLeft(gameState))
                    return gameState;

                tileMapService.unpaintTetrisPiece(gameState);
                currentPiece.setPoints(updatedPoints);
                tileMapService.paintTetrisPiece(gameState);

                break;
            }
            case "ARROWRIGHT": {
                List<Point> updatedPoints = this.movePoints(currentPiece, Direction.RIGHT);

                if (isObstructedRight(gameState))
                    return gameState;

                tileMapService.unpaintTetrisPiece(gameState);
                currentPiece.setPoints(updatedPoints);
                tileMapService.paintTetrisPiece(gameState);
                break;
            }
            case "ARROWDOWN": {
                List<Point> updatedPoints = this.movePoints(currentPiece, Direction.DOWN);

                if (isObstructedBelow(gameState))
                    return gameState;

                tileMapService.unpaintTetrisPiece(gameState);
                currentPiece.setPoints(updatedPoints);
                tileMapService.paintTetrisPiece(gameState);
                break;
            }
            case "ARROWUP":
                tetrisPieceService.rotateTetrisPiece(currentPiece);
                break;
        }

        return gameState;
    }


    // TO DO: bundle all three isObstructed methods into a single method
    private boolean isObstructedLeft(GameState gameState) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        TileMap tileMap = gameState.getTileMap();

        for (Point p : currentPiece.getPoints()) {
            int leftX = p.x - 1;
            Optional<Tile> oTileLeft = tileMap.getTiles().stream().filter(t -> t.getY() == p.getY() && t.getX() == leftX).findFirst();

            if (oTileLeft.isEmpty())
                return true;

            Tile tileLeft = oTileLeft.get();

            if (currentPiece.getPoints().contains(new Point(tileLeft.getX(), tileLeft.getY()))) // If tile left is part of the same tetrisPiece
                continue;

            if (!tileLeft.getContent().equals("blank"))
                return true;

        }
        return false;
    }

    private boolean isObstructedRight(GameState gameState) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        TileMap tileMap = gameState.getTileMap();

        for (Point p : currentPiece.getPoints()) {
            int rightX = p.x + 1;
            Optional<Tile> oTileRight = tileMap.getTiles().stream().filter(t -> t.getY() == p.getY() && t.getX() == rightX).findFirst();

            if (oTileRight.isEmpty()) // Out of bounds
                return true;

            Tile tileRight = oTileRight.get();

            if (currentPiece.getPoints().contains(new Point(tileRight.getX(), tileRight.getY()))) // If tile right is part of the same tetrisPiece
                continue;

            if (!tileRight.getContent().equals("blank"))
                return true;
        }
        return false;
    }

    private boolean isObstructedBelow(GameState gameState) {
        TetrisPiece tetrisPiece = gameState.getCurrentPiece();
        TileMap tileMap = gameState.getTileMap();

        for (Point p : tetrisPiece.getPoints()) {
            int nextY = p.y + 1;
            Optional<Tile> oTileBelow = tileMap.getTiles().stream().filter(t -> t.getY() == nextY && t.getX() == p.x).findFirst();

            if (oTileBelow.isEmpty()) // If there is no tile below, AKA bottom boundary is reached
                return true;

            Tile tileBelow = oTileBelow.get();

            if (tetrisPiece.getPoints().contains(new Point(tileBelow.getX(), tileBelow.getY()))) // If tile below is part of the same tetrisPiece
                continue;

            if (!tileBelow.getContent().equals("blank")) // If tile below is already occupied
                return true;
        }

        return false;
    }

    public GameState moveComputer(GameState gameState) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        List<Point> updatedPoints = this.movePoints(currentPiece, Direction.DOWN);

        if (isObstructedBelow(gameState)) {
            gameState.setCurrentPiece(tetrisPieceService.getNextTetrisPiece());
            return gameState;
        }

        tileMapService.unpaintTetrisPiece(gameState);
        currentPiece.setPoints(updatedPoints);
        tileMapService.paintTetrisPiece(gameState);

        return gameState;
    }

    public List<Point> movePoints(TetrisPiece tetrisPiece, Direction direction) {
        int xOffset = (direction == Direction.RIGHT) ? 1 : (direction == Direction.LEFT) ? -1 : 0;
        int yOffset = (direction == Direction.DOWN) ? 1 : 0;

        // Update the coordinates of the TetrisPiece points
        List<Point> updatedPoints = new ArrayList<>();
        for (Point point : tetrisPiece.getPoints()) {
            updatedPoints.add(new Point(point.x + xOffset, point.y + yOffset));
        }

        return updatedPoints;
    }
}
