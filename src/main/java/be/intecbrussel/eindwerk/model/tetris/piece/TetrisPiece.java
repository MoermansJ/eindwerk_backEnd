package be.intecbrussel.eindwerk.model.tetris.piece;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public abstract class TetrisPiece {
    //properties
    @Id
    protected Long id;
    
    protected String[][] shape;

    @ElementCollection
    protected List<Point> points;


    //constructors
    public TetrisPiece(String[][] shape) {
        this.shape = shape;
        this.points = this.getCollisionPoints(shape);
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
}
