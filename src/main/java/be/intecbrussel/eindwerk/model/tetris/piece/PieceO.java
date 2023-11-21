package be.intecbrussel.eindwerk.model.tetris.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PieceO extends TetrisPiece {
    @Id
    private Long id;

    public PieceO() {
        super(new String[][]{
                {"2", "2"},
                {"2", "2"},
        });
    }
}
