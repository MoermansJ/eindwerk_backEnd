package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.piece.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TetrisPieceService {
    // Custom methods
    public TetrisPiece rotateTetrisPiece(TetrisPiece tetrisPiece) {
        // Rotating shape
        List<String> rotatedShape = this.rotateShape(tetrisPiece);
        tetrisPiece.setShape(rotatedShape);

        // Getting collision points from new shape
        List<Point> rotatedPoints = tetrisPiece.getPointsFromShape(rotatedShape);
        tetrisPiece.setPoints(rotatedPoints);

        return tetrisPiece;
    }

    private List<String> rotateShape(TetrisPiece tetrisPiece) {
        int rotationCounter = tetrisPiece.getRotationCounter() + 1;
        tetrisPiece.setRotationCounter(rotationCounter);
        List<String> originalShape = tetrisPiece.getShape();
        int rows = originalShape.size();
        int cols = originalShape.get(0).length();

        List<String> rotatedShape = new ArrayList<>();

        for (int r = 0; r < rotationCounter; r++) { // Amount of rotations
            for (int j = 0; j < cols; j++) {        // Perform the rotation
                StringBuilder newRow = new StringBuilder();
                for (int i = rows - 1; i >= 0; i--) {
                    newRow.append(originalShape.get(i).charAt(j));
                }
                rotatedShape.add(newRow.toString());
            }
        }

        return rotatedShape;
    }

    public TetrisPiece getNextTetrisPiece() {
        // TO DO : opsplitsen naar eigen PieceUtil klasse
        Random random = new Random();
        int randomInt = random.nextInt(0, 7);
        // Input van Bogdan: bereik van nummers verbreden (van 0 - 10)

        switch (randomInt) {
            case 0:
                return new PieceI();
            case 1:
                return new PieceJ();
            case 2:
                return new PieceL();
            case 3:
                return new PieceO();
            case 4:
                return new PieceS();
            case 5:
                return new PieceT();
            case 6:
                return new PieceZ();
        }
        return new PieceZ();
    }
}
