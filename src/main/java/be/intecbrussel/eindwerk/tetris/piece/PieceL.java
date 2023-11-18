package be.intecbrussel.eindwerk.tetris.piece;

public class PieceL implements ITetrisPiece {
    private String[][] shape = {
            {"_", "3", "_"},
            {"_", "3", "_"},
            {"_", "3", "3"},
    };

    @Override
    public String[][] getShape() {
        return shape;
    }
}
