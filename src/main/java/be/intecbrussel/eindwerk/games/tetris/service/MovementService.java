package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.Direction;
import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.*;

@Service
public class MovementService {
    private TetrisPieceService tetrisPieceService;
    private TileMapService tileMapService;

    public MovementService(TetrisPieceService tetrisPieceService, TileMapService tileMapService) {
        this.tetrisPieceService = tetrisPieceService;
        this.tileMapService = tileMapService;
    }

    public Direction getDirectionFromString(String key) {
        switch (key) {
            case "ARROWLEFT":
                return Direction.LEFT;
            case "ARROWRIGHT":
                return Direction.RIGHT;
            case "ARROWUP":
                return Direction.UP;
            case "COMPUTERMOVE":
            case "ARROWDOWN":
            default:
                return Direction.DOWN;
        }
    }

    public List<Point> getNextPoints(TetrisPiece tetrisPiece, Direction direction) {
        // Calculating offset coëfficient
        int xOffset = (direction == Direction.RIGHT) ? 1 : (direction == Direction.LEFT) ? -1 : 0;
        int yOffset = (direction == Direction.DOWN) ? -1 : 0;

        // Update the coordinates of the TetrisPiece points
        List<Point> updatedPoints = new ArrayList<>();
        for (Point point : tetrisPiece.getPoints()) {
            updatedPoints.add(new Point(point.x + xOffset, point.y + yOffset));
        }

        return updatedPoints;
    }

    public boolean detectCollision(GameState gameState, List<Point> points) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        TileMap tileMap = gameState.getTileMap();

        for (Point point : points) {
            Optional<Tile> oNextTile = findTileByPoint(tileMap, point);

            // If there is no tile in the next position, AKA boundary is reached
            if (oNextTile.isEmpty())
                return true;

            Tile nextTile = oNextTile.get();

            // If tile in the next position is part of the same tetrisPiece
            if (currentPiece.getPoints().contains(nextTile.getPoint())) {
                continue;
            }

            // If tile in the next position is already occupied
            if (!nextTile.getContent().equals("blank"))
                return true;
        }

        return false;
    }

    private Optional<Tile> findTileByPoint(TileMap tileMap, Point point) {
        return tileMap.getTiles().stream().filter(tile -> tile.getPoint().equals(point)).findFirst();
    }

    public void executeMovement(GameState gameState, Direction direction) {
        tileMapService.unpaintCurrentPiece(gameState);

        if (direction == Direction.UP) {
            tetrisPieceService.rotate(gameState.getCurrentPiece(), gameState.getTileMap());
            tileMapService.paintCurrentPiece(gameState);
            return;
        }

        List<Point> nextPoints = this.getNextPoints(gameState.getCurrentPiece(), direction);
        if (this.detectCollision(gameState, nextPoints)) {
            tileMapService.paintCurrentPiece(gameState);

            if (direction == Direction.DOWN) {
                tileMapService.removeCompletedRowsFromTileMap(gameState.getTileMap());
                TetrisPiece newTetrisPiece = tetrisPieceService.getNextTetrisPiece();
                gameState.setCurrentPiece(newTetrisPiece);
                tileMapService.positionNewTetrisPiece(gameState);
                tileMapService.paintCurrentPiece(gameState);
                return;
            }
            return;
        }

        gameState.getCurrentPiece().setPoints(nextPoints);
        tileMapService.paintCurrentPiece(gameState);
    }
}
