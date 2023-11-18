package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

@Data
public class PieceO implements ITetrisPiece {
    private String[][] shape = {
            {"2", "2"},
            {"2", "2"},
    };
}
