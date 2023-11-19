package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

public class PieceJ extends TetrisPiece {
    public PieceJ() {
        super(new String[][]{
                {"_", "4", "_"},
                {"_", "4", "_"},
                {"4", "4", "_"},
        });
    }
}
