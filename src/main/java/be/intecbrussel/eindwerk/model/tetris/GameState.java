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
            this.tileMap = new TileMap(16, 7, currentPiece);
    }


    //custom methods
    public void movePlayer(String key) {
        switch (key) { // TO DO: streamline & bundle this logic
            case "ARROWLEFT": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.LEFT);

                if (isObstructedLeft(currentPiece))
                    return;

                tileMap.clearTetrisPiece(currentPiece);
                currentPiece.setPoints(updatedPoints);
                tileMap.paintTetrisPiece(currentPiece);
                break;
            }
            case "ARROWRIGHT": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.RIGHT);

                if (isObstructedRight(currentPiece))
                    return;

                tileMap.clearTetrisPiece(currentPiece);
                currentPiece.setPoints(updatedPoints);
                tileMap.paintTetrisPiece(currentPiece);
                break;
            }
            case "ARROWDOWN": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.DOWN);

                if (isObstructedBelow(currentPiece))
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

        if (isObstructedBelow(currentPiece)) {
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

    private TetrisPiece getNextTetrisPiece() {
        // TO DO : opsplitsen naar eigen PieceUtil klasse
        //VRAGEN AAN MANUEL VOOR INPUT
        //implement static factory methods for the Piece classes? Manuel: no -> keep switch
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

    private boolean isObstructedLeft(TetrisPiece tetrisPiece) {
        for (Point p : tetrisPiece.getPoints()) {
            int leftX = p.x - 1;
            Optional<Tile> oTileLeft = this.tileMap.getTiles().stream().filter(t -> t.getY() == p.getY() && t.getX() == leftX).findFirst();

            if (oTileLeft.isEmpty())
                return true;

            Tile tileLeft = oTileLeft.get();

            if (tetrisPiece.getPoints().contains(new Point(tileLeft.getX(), tileLeft.getY()))) // If tile left is part of the same tetrisPiece
                continue;

            if (!tileLeft.getContent().equals("white"))
                return true;

        }
        return false;
    }

    private boolean isObstructedRight(TetrisPiece tetrisPiece) {
        for (Point p : tetrisPiece.getPoints()) {
            int rightX = p.x + 1;
            Optional<Tile> oTileRight = this.tileMap.getTiles().stream().filter(t -> t.getY() == p.getY() && t.getX() == rightX).findFirst();

            if (oTileRight.isEmpty())
                return true;

            Tile tileRight = oTileRight.get();

            if (tetrisPiece.getPoints().contains(new Point(tileRight.getX(), tileRight.getY()))) // If tile right is part of the same tetrisPiece
                continue;

            if (!tileRight.getContent().equals("white"))
                return true;
        }
        return false;
    }

    private boolean isObstructedBelow(TetrisPiece tetrisPiece) {
        for (Point p : tetrisPiece.getPoints()) {
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


}

