package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class PieceI extends TetrisPiece {
    // Properties
    @Id
    private Long id;

    private String content = this.getClass().getSimpleName(); //TO DO: implement CSS classes based on Java class-name


    // Constructors
    public PieceI() {
        super(getShapeAsPointList());
    }


    // Custom methods
    private static List<Point> getShapeAsPointList() {
        return new ArrayList<>(Arrays.asList(
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 0),
                new Point(3, 0)
        ));
    }
}
