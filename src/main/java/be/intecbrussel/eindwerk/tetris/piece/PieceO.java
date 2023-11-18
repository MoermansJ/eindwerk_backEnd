package be.intecbrussel.eindwerk.tetris.piece;

public class PieceO implements ITetrisPiece {
    private String[][] shape = {
            {"2", "2"},
            {"2", "2"},
    };

    @Override
    public String[][] getShape() {
        return shape;
    }
}
