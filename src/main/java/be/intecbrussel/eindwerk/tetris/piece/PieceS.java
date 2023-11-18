package be.intecbrussel.eindwerk.tetris.piece;

public class PieceS implements ITetrisPiece {
    private String[][] shape = {
            {"_", "6", "6"},
            {"6", "6", "_"},
            {"_", "_", "_"},
    };

    @Override
    public String[][] getShape() {
        return shape;
    }
}
