package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PieceO extends TetrisPiece {
    @Id
    private Long id;

    private String content = "yellow";

    public PieceO() {
        super(new String[][]{
                {"2", "2"},
                {"2", "2"},
        });
    }
}
