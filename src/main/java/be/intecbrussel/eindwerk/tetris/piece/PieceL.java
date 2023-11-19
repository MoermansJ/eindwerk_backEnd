package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

public class PieceL extends TetrisPiece {
    public PieceL() {
        super(new String[][]{
                {"_", "3", "_"},
                {"_", "3", "_"},
                {"_", "3", "3"},
        });
    }
}
