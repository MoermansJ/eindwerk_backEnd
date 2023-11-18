package be.intecbrussel.eindwerk.tetris.piece;

import lombok.Data;

public interface ITetrisPiece {
    public String[][] getShape();

    public void setShape(String[][] newShape);
}
