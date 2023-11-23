package be.intecbrussel.eindwerk.model.tetris;

import be.intecbrussel.eindwerk.model.tetris.piece.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_gamestate")
public class GameState {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @OneToOne(cascade = CascadeType.MERGE)
    private TileMap tileMap;

    @OneToOne(cascade = CascadeType.MERGE)
    private TetrisPiece currentPiece;

    private LocalDateTime timeStamp;

    public enum Direction {
        LEFT, RIGHT, DOWN
    }


    //constructors
    public GameState() {
        if (currentPiece == null)
            this.currentPiece = new PieceZ();

        if (this.tileMap == null)
            this.tileMap = new TileMap(16, 16, currentPiece);
    }


    //custom methods
    public void movePlayer(String key) {
        switch (key) { // TO DO: streamline & bundle this logic
            case "ARROWLEFT": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.LEFT);

                if (isOutOfBounds(currentPiece))
                    return;

                tileMap.clearTetrisPiece(currentPiece);
                currentPiece.setPoints(updatedPoints);
                tileMap.paintTetrisPiece(currentPiece);
                break;
            }
            case "ARROWRIGHT": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.RIGHT);

                if (isOutOfBounds(currentPiece))
                    return;

                tileMap.clearTetrisPiece(currentPiece);
                currentPiece.setPoints(updatedPoints);
                tileMap.paintTetrisPiece(currentPiece);
                break;
            }
            case "ARROWDOWN": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.DOWN);

                if (isOutOfBounds(currentPiece))
                    return;

                tileMap.clearTetrisPiece(currentPiece);
                currentPiece.setPoints(updatedPoints);
                tileMap.paintTetrisPiece(currentPiece);
                break;
            }
            case "ARROWUP":
                tileMap.clearTetrisPiece(currentPiece);
                currentPiece.rotate();
                tileMap.paintTetrisPiece(currentPiece);
                break;
        }
    }

    public void moveComputer() { // Computermove is currently disabled from having any effect on the game; UNCOMMENT TO ENABLE!
        List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.DOWN);

        if (isOutOfBounds(currentPiece))
            return;

        if (isObstructed(currentPiece)) {
            currentPiece = this.getNextTetrisPiece();
            return;
        }

        tileMap.clearTetrisPiece(currentPiece);
        this.currentPiece.setPoints(updatedPoints);
        tileMap.paintTetrisPiece(currentPiece);
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

    private boolean isOutOfBounds(TetrisPiece tetrisPiece) {
        List<Point> points = tetrisPiece.getPoints();
        int maxX = this.tileMap.getWidth();
        int maxY = this.tileMap.getHeight();

        for (Point p : points) {
            if (p.x < 0) // If at left border
                return true;

            if (p.x >= maxX) // If at right border
                return true;

            if (p.y >= maxY) // If at bottom
                return true;
        }
        return false;
    }

    private boolean isObstructed(TetrisPiece tetrisPiece) {
        List<Point> points = tetrisPiece.getPoints();

        for (Point p : points) {
            int nextY = p.y + 1;
            Optional<Tile> oTileBelow = this.tileMap.getTiles().stream().filter(t -> t.getY() == nextY && t.getX() == p.x).findFirst();

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

    private TetrisPiece getNextTetrisPiece() {
        //VRAGEN AAN MANUEL VOOR INPUT
        //implement static factory methods for the Piece classes?
        Random random = new Random();
        int randomInt = random.nextInt(0, 7);

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
}

