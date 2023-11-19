package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

public class PieceS extends TetrisPiece {
    public PieceS() {
        super(new String[][]{
                {"_", "6", "6"},
                {"6", "6", "_"},
                {"_", "_", "_"},
        });
    }
}
