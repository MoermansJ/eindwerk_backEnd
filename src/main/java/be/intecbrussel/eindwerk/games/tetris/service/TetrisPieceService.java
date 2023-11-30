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
    public List<Point> rotate(TetrisPiece tetrisPiece) {
        int rotations = tetrisPiece.getRotationCounter();
        tetrisPiece.setRotationCounter(++rotations);
        List<Point> rotatedPoints = new ArrayList<>(tetrisPiece.getPoints());

        Point center = this.calculateCenter(rotatedPoints);

        // Find the minimum Y value before rotating
        int minYBefore = (int) rotatedPoints.stream().mapToDouble(Point::getY).min().orElse(0);

        // Rotating 90° clockwise
        for (Point point : rotatedPoints) {
            int tempX = point.x - center.x;
            int tempY = point.y - center.y;

            point.x = center.x + tempY;
            point.y = center.y - tempX;
        }

        // Find the minimum Y value after rotating
        int minYAfter = (int) rotatedPoints.stream().mapToDouble(Point::getY).min().orElse(0);

        // Adjust all Y coordinates to ensure the minimum Y remains the same
        int yAdjustment = minYBefore - minYAfter;
        rotatedPoints.forEach(point -> point.y += yAdjustment);

        return rotatedPoints;
    }

    private Point calculateCenter(List<Point> points) {
        int totalX = 0;
        int totalY = 0;

        for (Point point : points) {
            totalX += point.x;
            totalY += point.y;
        }

        return new Point(totalX / points.size(), totalY / points.size());
    }

    public TetrisPiece getNextTetrisPiece() {
        Random random = new Random();
        int randomInt = random.nextInt(0, 7);

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
