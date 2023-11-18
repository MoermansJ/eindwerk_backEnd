package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

@Data
public class PieceJ implements ITetrisPiece {
    private String[][] shape = {
            {"_", "4", "_"},
            {"_", "4", "_"},
            {"4", "4", "_"},
    };
}
