package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class PieceZ extends TetrisPiece {
    // Properties
    @Id
    private Long id;

    private String content = this.getClass().getSimpleName();


    // Constructors
    public PieceZ() {
        super(generateShape());
    }


    // Custom methods
    private static List<Point> generateShape() {
        return new ArrayList<>(Arrays.asList(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 1),
                new Point(2, 1)
        ));
    }
}
