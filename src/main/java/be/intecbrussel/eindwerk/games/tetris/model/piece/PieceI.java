package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PieceI extends TetrisPiece {
    @Id
    private Long id;

    private String content = "cyan";

    public PieceI() {
        super(new String[][]{
                {"_", "5", "_", "_"},
                {"_", "5", "_", "_"},
                {"_", "5", "_", "_"},
                {"_", "5", "_", "_"}
        });
    }
}
