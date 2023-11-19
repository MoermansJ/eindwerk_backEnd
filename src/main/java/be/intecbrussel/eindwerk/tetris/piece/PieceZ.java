package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;
import lombok.NoArgsConstructor;

public class PieceZ extends TetrisPiece {
    public PieceZ() {
        super(new String[][]{
                {"7", "7", "_"},
                {"_", "7", "7"},
                {"_", "_", "_"}
        });
    }
}
