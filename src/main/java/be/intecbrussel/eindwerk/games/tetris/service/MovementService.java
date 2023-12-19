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
    private TetrisPieceGenerator tetrisPieceGenerator;

    public MovementService(TetrisPieceService tetrisPieceService, TileMapService tileMapService, TetrisPieceGenerator tetrisPieceGenerator) {
        this.tetrisPieceService = tetrisPieceService;
        this.tileMapService = tileMapService;
        this.tetrisPieceGenerator = tetrisPieceGenerator;
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
        int xOffset = (direction == Direction.RIGHT) ? 1 : (direction == Direction.LEFT) ? -1 : 0;
        int yOffset = (direction == Direction.DOWN) ? -1 : 0;

        List<Point> updatedPoints = new ArrayList<>();
        for (Point point : tetrisPiece.getPoints()) {
            updatedPoints.add(new Point(point.x + xOffset, point.y + yOffset));
        }

        return updatedPoints;
    }

    public boolean isNextMovePossible(GameState gameState, List<Point> points) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        TileMap tileMap = gameState.getTileMap();


        for (Point point : points) {
            Optional<Tile> oNextTile = findTileByPoint(tileMap, point);

            // If there is no tile in the next position, AKA boundary is reached
            if (oNextTile.isEmpty())
                return false;

            Tile nextTile = oNextTile.get();

            // If tile in the next position is part of the same tetrisPiece
            if (currentPiece.getPoints().contains(nextTile.getPoint())) {
                continue;
            }

            // If tile in the next position is already occupied
            if (!nextTile.getContent().equals("blank"))
                return false;
        }

        return true;
    }

    private Optional<Tile> findTileByPoint(TileMap tileMap, Point point) {
        return tileMap.getTiles().stream().filter(tile -> tile.getPoint().equals(point)).findFirst();
    }

    public void executeMovement(GameState gameState, Direction direction) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        tileMapService.unpaintCurrentPiece(gameState);

        if (direction == Direction.UP) {
            handleMoveUp(gameState);
            return;
        }

        List<Point> nextPoints = this.getNextPoints(currentPiece, direction);

        if (!this.isNextMovePossible(gameState, nextPoints)) {
            tileMapService.paintCurrentPiece(gameState);

            if (direction == Direction.DOWN) {
                handleMoveDown(gameState);
            }

            return;
        }


        gameState.getCurrentPiece().setPoints(nextPoints);
        tileMapService.paintCurrentPiece(gameState);
    }

    private void handleMoveUp(GameState gameState) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        TileMap tileMap = gameState.getTileMap();

        tetrisPieceService.rotateTetrisPiece(currentPiece, tileMap);
        tileMapService.paintCurrentPiece(gameState);
    }

    private void handleMoveDown(GameState gameState) {
        TileMap tileMap = gameState.getTileMap();

        List<Integer> seededGenerationPattern = gameState.getSeededGenerationPattern();
        int firstElement = seededGenerationPattern.remove(0);
        TetrisPiece newTetrisPiece = tetrisPieceGenerator.getNextTetrisPiece(firstElement);
        seededGenerationPattern.add(firstElement);

        tileMapService.removeCompletedRowsFromTileMap(tileMap);
        gameState.setCurrentPiece(newTetrisPiece);
        tileMapService.positionNewTetrisPiece(gameState);
        tileMapService.paintCurrentPiece(gameState);
    }
}
