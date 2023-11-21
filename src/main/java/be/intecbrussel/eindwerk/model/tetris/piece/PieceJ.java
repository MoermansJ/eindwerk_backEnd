package be.intecbrussel.eindwerk.model.tetris.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PieceJ extends TetrisPiece {
    @Id
    private Long id;

    public PieceJ() {
        super(new String[][]{
                {"_", "4", "_"},
                {"_", "4", "_"},
                {"4", "4", "_"},
        });
    }
}
