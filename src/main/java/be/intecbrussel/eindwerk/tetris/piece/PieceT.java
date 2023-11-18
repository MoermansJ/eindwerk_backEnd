package be.intecbrussel.eindwerk.tetris.piece;

public class PieceT implements ITetrisPiece {
    private String[][] shape = {
            {"_", "_", "_"},
            {"1", "1", "1"},
            {"_", "1", "_"}
    };

    @Override
    public String[][] getShape() {
        return shape;
    }
}
