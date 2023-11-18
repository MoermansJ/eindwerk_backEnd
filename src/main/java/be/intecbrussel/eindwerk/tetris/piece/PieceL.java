package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

@Data
public class PieceL implements ITetrisPiece {
    private String[][] shape = {
            {"_", "3", "_"},
            {"_", "3", "_"},
            {"_", "3", "3"},
    };

}
