package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class TetrisPiece {
    //properties
    protected String[][] shape;
    protected List<Point> points;


    //constructors
    public TetrisPiece(String[][] shape) {
        this.shape = shape;
        this.points = this.getFilledCells(shape);
    }


    //custom methods - with implementation
    public void rotateShape() {
        String[][] originalShape = this.getShape();
        int rows = originalShape.length;
        int cols = originalShape[0].length;

        // Create a new 2D array for the rotated shape
        String[][] rotatedShape = new String[cols][rows];

        // Perform the rotation
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedShape[j][rows - 1 - i] = originalShape[i][j];
            }
        }

        // Updating points
        this.points = this.getFilledCells(rotatedShape);
    }

    public List<Point> getFilledCells(String[][] shape) {
        List<Point> filledCells = new ArrayList<>();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (!shape[i][j].equals("_")) {
                    filledCells.add(new Point(j, i));
                }
            }
        }

        return filledCells;
    }
}
