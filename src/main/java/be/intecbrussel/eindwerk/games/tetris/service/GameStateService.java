package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GameStateService {
    //properties
    @Autowired
    private TileMapService tileMapService;


    //custom methods
    public GameState movePlayer(GameState gameState, String key) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();

        switch (key) { // TO DO: streamline & bundle this logic
            case "ARROWLEFT": {
                java.util.List<Point> updatedPoints = this.movePoints(currentPiece, GameState.Direction.LEFT);

                if (isObstructedLeft(gameState))
                    return gameState;

                tileMapService.clearTetrisPiece(gameState);
                currentPiece.setPoints(updatedPoints);
                tileMapService.paintTetrisPiece(gameState);
                break;
            }
            case "ARROWRIGHT": {
                java.util.List<Point> updatedPoints = this.movePoints(currentPiece, GameState.Direction.RIGHT);

                if (isObstructedRight(gameState))
                    return gameState;

                tileMapService.clearTetrisPiece(gameState);
                currentPiece.setPoints(updatedPoints);
                tileMapService.paintTetrisPiece(gameState);
                break;
            }
            case "ARROWDOWN": {
                List<Point> updatedPoints = this.movePoints(currentPiece, GameState.Direction.DOWN);

                if (isObstructedBelow(gameState))
                    return gameState;

                tileMapService.clearTetrisPiece(gameState);
                currentPiece.setPoints(updatedPoints);
                tileMapService.paintTetrisPiece(gameState);
                break;
            }
            case "ARROWUP":
                tileMapService.clearTetrisPiece(gameState);
                currentPiece.rotate();
                tileMapService.paintTetrisPiece(gameState);
                break;
        }

        return gameState;
    }

    public GameState moveComputer(GameState gameState) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        TileMap tileMap = gameState.getTileMap();
        List<Point> updatedPoints = this.movePoints(currentPiece, GameState.Direction.DOWN);

        if (isObstructedBelow(gameState)) {
            gameState.setCurrentPiece(this.getNextTetrisPiece());
            return gameState;
        }

        tileMapService.clearTetrisPiece(gameState);
        currentPiece.setPoints(updatedPoints);
        tileMapService.paintTetrisPiece(gameState);

        return gameState;
    }

    public List<Point> movePoints(TetrisPiece tetrisPiece, GameState.Direction direction) {
        int xOffset = (direction == GameState.Direction.RIGHT) ? 1 : (direction == GameState.Direction.LEFT) ? -1 : 0;
        int yOffset = (direction == GameState.Direction.DOWN) ? 1 : 0;

        // Update the coordinates of the TetrisPiece points
        List<Point> updatedPoints = new ArrayList<>();
        for (Point point : tetrisPiece.getPoints()) {
            updatedPoints.add(new Point(point.x + xOffset, point.y + yOffset));
        }

        return updatedPoints;
    }

    public TetrisPiece getNextTetrisPiece() {
        // TO DO : opsplitsen naar eigen PieceUtil klasse
        Random random = new Random();
        int randomInt = random.nextInt(0, 7);
        // Input van Bogdan: bereik van nummers verbreden (van 0 - 10)

        switch (randomInt) {
            case 0:
                return new PieceI();
            case 1:
                return new PieceJ();
            case 2:
                return new PieceL();
            case 3:
                return new PieceO();
            case 4:
                return new PieceS();
            case 5:
                return new PieceT();
            case 6:
                return new PieceZ();
        }
        return new PieceZ();
    }

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

            if (!tileLeft.getContent().equals("white"))
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

            if (oTileRight.isEmpty())
                return true;

            Tile tileRight = oTileRight.get();

            if (currentPiece.getPoints().contains(new Point(tileRight.getX(), tileRight.getY()))) // If tile right is part of the same tetrisPiece
                continue;

            if (!tileRight.getContent().equals("white"))
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

            if (!tileBelow.getContent().equals("white")) // If tile below is already coloured, AKA there is a piece already placed
                return true;
        }

        return false;
    }
}
