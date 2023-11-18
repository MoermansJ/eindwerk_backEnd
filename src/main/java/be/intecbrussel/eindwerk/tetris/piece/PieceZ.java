package be.intecbrussel.eindwerk.tetris.piece;

public class PieceZ implements ITetrisPiece {
    private String[][] shape = {
            {"7", "7", "_"},
            {"_", "7", "7"},
            {"_", "_", "_"}
    };

    @Override
    public String[][] getShape() {
        return shape;
    }
}
