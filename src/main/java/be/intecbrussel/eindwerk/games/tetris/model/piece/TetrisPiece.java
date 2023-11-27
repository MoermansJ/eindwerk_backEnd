package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public abstract class TetrisPiece {
    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ElementCollection
    protected List<Point> points;

    @ElementCollection
    protected List<String> shape;

    protected int rotationCounter;

    protected String content;


    // Constructors
    public TetrisPiece(List<String> shape) {
        this.shape = shape;
        this.points = this.getPointsFromShape(shape);
    }


    // Custom methods
    public List<Point> getPointsFromShape(List<String> shape) {
        List<Point> filledCells = new ArrayList<>();

        // Not using lambda's here because I need the index position
        for (int row = 0; row < shape.size(); row++) {
            for (int col = 0; col < shape.get(row).length(); col++) {
                char currentChar = shape.get(row).charAt(col);
                if (currentChar != '_')
                    filledCells.add(new Point(col, row));
            }
        }

        return filledCells;
    }
}
