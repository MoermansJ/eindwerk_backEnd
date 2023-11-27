package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class PieceO extends TetrisPiece {
    // Properties
    @Id
    private Long id;

    private String content = this.getClass().getSimpleName();


    // Constructors
    public PieceO() {
        super(generateShape());
    }


    // Custom methods
    private static List<String> generateShape() {
        return new ArrayList<>(Arrays.asList(
                "PP",
                "PP"
        ));
    }
}
