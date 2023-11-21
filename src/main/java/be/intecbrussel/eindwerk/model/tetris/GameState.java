package be.intecbrussel.eindwerk.model.tetris;

import be.intecbrussel.eindwerk.model.tetris.piece.PieceZ;
import be.intecbrussel.eindwerk.model.tetris.piece.TetrisPiece;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@Table(name = "tb_gamestate")
public class GameState {
    //properties
    @Id
    private Long sessionId;

//    private TetrisPiece piece;

    @OneToOne
    private TileMap tileMap;

    @OneToOne
    private TetrisPiece tetrisPiece;

    private enum Direction {
        LEFT, RIGHT, DOWN
    }


    //constructors
    public GameState() {
        this.tileMap = new TileMap(16, 32);
        this.tetrisPiece = new PieceZ();
        tileMap.insertPiece();
    }


    //custom methods
    public void movePlayer(String key) {
        switch (key) {
            case "ARROWLEFT":
                paintPoints("_"); //clearing old
                movePoints(Direction.LEFT);//moving coordinates based on user input
                paintPoints("P"); //painting new
                break;
            case "ARROWRIGHT":
                paintPoints("_"); //clearing old
                movePoints(Direction.RIGHT);//moving coordinates based on user input
                paintPoints("P"); //painting new
                break;
            case "ARROWDOWN":
                paintPoints("_"); //clearing old
                movePoints(Direction.DOWN);//moving coordinates based on user input
                paintPoints("P"); //painting new
                break;
            case "ARROWUP":
                paintPoints("_"); //clearing old
//                piece.rotateShape();
                paintPoints("P"); //painting new
                break;
        }
    }

    private static void paintPoints(String character) {
//        for (Point point : piece.getPoints()) {
//            int row = point.y;
//            int col = point.x;
//
//            if (row >= 0 && row < tileMap.length && col >= 0 && col < tileMap[0].length) {
//                tileMap[row][col] = character;
//            }
//        }
    }

    private static void movePoints(Direction direction) {
//        int xOffset = (direction == Direction.RIGHT) ? 1 : (direction == Direction.LEFT) ? -1 : 0;
//        int yOffset = (direction == Direction.DOWN) ? 1 : 0;
//
//        // Update the coordinates of the TetrisPiece points
//        List<Point> updatedPoints = new ArrayList<>();
//        for (Point point : piece.getPoints()) {
//            updatedPoints.add(new Point(point.x + xOffset, point.y + yOffset));
//        }
//        piece.setPoints(updatedPoints);
    }


}
