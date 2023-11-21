package be.intecbrussel.eindwerk.model.tetris.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class PieceL extends TetrisPiece {
    @Id
    private Long id;


    public PieceL() {
        super(new String[][]{
                {"_", "3", "_"},
                {"_", "3", "_"},
                {"_", "3", "3"},
        });
    }
}
