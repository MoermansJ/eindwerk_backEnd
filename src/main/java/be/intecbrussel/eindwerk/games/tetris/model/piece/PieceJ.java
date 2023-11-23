package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PieceJ extends TetrisPiece {
    @Id
    private Long id;

    private String content = "blue";

    public PieceJ() {
        super(new String[][]{
                {"_", "4", "_"},
                {"_", "4", "_"},
                {"4", "4", "_"},
        });
    }
}
