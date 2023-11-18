package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

@Data
public class PieceZ implements ITetrisPiece {
    private String[][] shape = {
            {"7", "7", "_"},
            {"_", "7", "7"},
            {"_", "_", "_"}
    };
}
