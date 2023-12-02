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
public class PieceT extends TetrisPiece {
    // Properties
    @Id
    private Long id;

    private String content = this.getClass().getSimpleName();


    // Constructors
    public PieceT() {
        super(getShapeAsPointList());
    }


    // Custom methods
    private static List<Point> getShapeAsPointList() {
        return new ArrayList<>(Arrays.asList(
                new Point(1, 0),
                new Point(0, 1),
                new Point(1, 1),
                new Point(2, 1)
        ));
    }
}
