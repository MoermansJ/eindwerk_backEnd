package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class PieceS extends TetrisPiece {
    @Id
    private Long id;

    private String content = "green";

    public PieceS() {
        super(new String[][]{
                {"_", "6", "6"},
                {"6", "6", "_"},
                {"_", "_", "_"},
        });
    }
}
