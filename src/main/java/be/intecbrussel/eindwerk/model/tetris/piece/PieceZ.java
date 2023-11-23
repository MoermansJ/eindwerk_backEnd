package be.intecbrussel.eindwerk.model.tetris.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PieceZ extends TetrisPiece {
    @Id
    private Long id;

    private String content = "purple";

    public PieceZ() {
        super(new String[][]{
                {"7", "7", "_"},
                {"_", "7", "7"},
                {"_", "_", "_"}
        });
    }
}
