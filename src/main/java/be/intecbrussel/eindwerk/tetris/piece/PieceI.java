package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

@Data
public class PieceI implements ITetrisPiece {
    private String[][] shape = {
            {"_", "5", "_", "_"},
            {"_", "5", "_", "_"},
            {"_", "5", "_", "_"},
            {"_", "5", "_", "_"}
    };
}
