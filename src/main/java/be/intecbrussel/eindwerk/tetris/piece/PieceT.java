package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

public class PieceT extends TetrisPiece {
    public PieceT() {
        super(new String[][]{
                {"_", "_", "_"},
                {"1", "1", "1"},
                {"_", "1", "_"}
        });
    }
}
