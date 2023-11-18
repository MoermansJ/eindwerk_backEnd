package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

@Data
public class PieceS implements ITetrisPiece {
    private String[][] shape = {
            {"_", "6", "6"},
            {"6", "6", "_"},
            {"_", "_", "_"},
    };
}
