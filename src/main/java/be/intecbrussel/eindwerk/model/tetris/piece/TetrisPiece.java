package be.intecbrussel.eindwerk.model.tetris.piece;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
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

    protected int rotationCounter;

    protected String content;

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
    public void rotate() {
        String[][] shape = convertShapeListTo2DArray(this.shape, width);
        this.shape = convertShape2DArrayToList(this.rotateShape(shape)); //to do: refactor rotateShape to use a List as a parameter
        points = this.rotatePoints(points);
    }

    private String[][] rotateShape(String[][] originalShape) {
//        String[][] originalShape = convertShapeListTo2DArray(shape, this.width);

        this.rotationCounter++;
        int rows = originalShape.length;
        int cols = originalShape[0].length;

        // Create a new 2D array for the rotated shape
        String[][] rotatedShape = new String[cols][rows];

        for (int r = 0; r < rotationCounter; r++) { // Amount of rotations
            for (int i = 0; i < rows; i++) { // Perform the rotation
                for (int j = 0; j < cols; j++) {
                    rotatedShape[j][rows - 1 - i] = originalShape[i][j];
                }
            }
        }

        return rotatedShape;
    }

    public List<Point> getCollisionPoints(String[][] shape) {
        List<Point> filledCells = new ArrayList<>();

        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (!shape[i][j].equals("_")) {
                    filledCells.add(new Point(j, i));
                }
            }
        }

        return filledCells;
    }

    private List<Point> rotatePoints(List<Point> oldPoints) {
        List<Point> rotatedPoints = new ArrayList<>();

        String[][] oldShape = convertShapeListTo2DArray(this.shape, this.width);
        String[][] rotatedShape = this.rotateShape(oldShape);
        List<Point> newShapePoints = getCollisionPoints(rotatedShape);

        for (int i = 0; i < this.points.size(); i++) {
            Point oldShapePoint = oldPoints.get(i);
            Point newShapePoint = newShapePoints.get(i);

            int deltaX = newShapePoint.x - oldShapePoint.x;
            int deltaY = newShapePoint.y - oldShapePoint.y;

            rotatedPoints.add(new Point(points.get(i).x + deltaX, points.get(i).y + deltaY));
        }

        return rotatedPoints;
    }

    public static String[][] convertShapeListTo2DArray(List<String> shapeList, int width) {
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

    public static List<String> convertShape2DArrayToList(String[][] shape) {
        List<String> result = new ArrayList<>();

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
