package be.intecbrussel.eindwerk.tetris.piece;

public class PieceJ implements ITetrisPiece {
    private String[][] shape = {
            {"_", "4", "_"},
            {"_", "4", "_"},
            {"4", "4", "_"},
    };

    @Override
    public String[][] getShape() {
        return shape;
    }
}
