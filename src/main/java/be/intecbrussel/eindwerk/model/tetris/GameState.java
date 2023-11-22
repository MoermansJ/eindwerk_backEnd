package be.intecbrussel.eindwerk.model.tetris;

import be.intecbrussel.eindwerk.model.tetris.piece.PieceL;
import be.intecbrussel.eindwerk.model.tetris.piece.PieceZ;
import be.intecbrussel.eindwerk.model.tetris.piece.TetrisPiece;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        switch (key) {
            case "ARROWLEFT": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.LEFT);

                if (isOutOfBounds(updatedPoints))
                    return;

                this.tileMap.updateTileMap(currentPiece.getPoints(), "| |"); //clearing old
                this.currentPiece.setPoints(updatedPoints);
                this.tileMap.updateTileMap(currentPiece.getPoints(), "P"); //painting new
                break;
            }
            case "ARROWRIGHT": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.RIGHT);

                if (isOutOfBounds(updatedPoints))
                    return;

                this.tileMap.updateTileMap(currentPiece.getPoints(), "| |"); //clearing old
                this.currentPiece.setPoints(updatedPoints);
                this.tileMap.updateTileMap(currentPiece.getPoints(), "P"); //painting new
                break;
            }
            case "ARROWDOWN": {
                List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.DOWN);

                if (isOutOfBounds(updatedPoints))
                    return;

                this.tileMap.updateTileMap(currentPiece.getPoints(), "| |"); //clearing old
                this.currentPiece.setPoints(updatedPoints);
                this.tileMap.updateTileMap(currentPiece.getPoints(), "P"); //painting new
                break;
            }
            case "ARROWUP":
                this.tileMap.updateTileMap(currentPiece.getPoints(), "| |"); //clearing old
                this.currentPiece.rotateShape();
                this.tileMap.updateTileMap(currentPiece.getPoints(), "P"); //painting new
                break;
        }
    }

    public void moveComputer() {
        List<Point> updatedPoints = this.movePoints(this.currentPiece, Direction.DOWN);

        if (isOutOfBounds(updatedPoints))
            return;

        this.tileMap.updateTileMap(currentPiece.getPoints(), "| |"); //clearing old
        this.currentPiece.setPoints(updatedPoints);
        this.tileMap.updateTileMap(currentPiece.getPoints(), "P"); //painting new
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

    private boolean isOutOfBounds(List<Point> points) {
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
}

