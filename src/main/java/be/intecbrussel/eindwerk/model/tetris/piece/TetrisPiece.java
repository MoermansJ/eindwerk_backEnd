package be.intecbrussel.eindwerk.model.tetris.piece;

import be.intecbrussel.eindwerk.model.tetris.GameState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public abstract class TetrisPiece {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ElementCollection
    protected List<Point> points;

    @ElementCollection
    protected List<String> shape;

    protected int width;


    //constructors
    public TetrisPiece(String[][] shape) {
        this(convertShape2DArrayToList(shape), shape[0].length);
    }

    public TetrisPiece(List<String> shape, int width) {
        this.shape = shape;
        this.width = width;
        this.points = this.getCollisionPoints(convertShapeListTo2DArray(this.shape, this.width));
    }


    //custom methods - with implementation
    public void rotateShape() {
        String[][] originalShape = convertShapeListTo2DArray(this.shape, this.width);
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
        this.points = this.getCollisionPoints(rotatedShape);
    }

    protected List<Point> getCollisionPoints(String[][] shape) {
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

    protected static String[][] convertShapeListTo2DArray(List<String> shapeList, int width) {
        int height = shapeList.size() / width;
        String[][] result = new String[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int index = i * width + j;
                if (index < shapeList.size()) {
                    result[i][j] = shapeList.get(index);
                }
            }
        }

        return result;
    }

    protected static List<String> convertShape2DArrayToList(String[][] shape) {
        List<String> result = new ArrayList<>();

        if (shape == null || shape.length == 0 || shape[0].length == 0) {
            // Handle invalid shape as needed
            return result;
        }

        int height = shape.length;
        int width = shape[0].length;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.add(shape[i][j]);
            }
        }

        return result;
    }


}
