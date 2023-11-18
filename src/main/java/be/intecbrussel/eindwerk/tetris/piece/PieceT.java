package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

@Data
public class PieceT implements ITetrisPiece {
    private String[][] shape = {
            {"_", "_", "_"},
            {"1", "1", "1"},
            {"_", "1", "_"}
    };
}
