package be.intecbrussel.eindwerk.tetris.piece;

public class PieceI implements ITetrisPiece {
    private String[][] shape = {
            {"_", "5", "_", "_"},
            {"_", "5", "_", "_"},
            {"_", "5", "_", "_"},
            {"_", "5", "_", "_"}
    };

    @Override
    public String[][] getShape() {
        return shape;
    }
}
