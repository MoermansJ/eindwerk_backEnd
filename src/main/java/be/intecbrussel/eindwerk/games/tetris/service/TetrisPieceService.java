package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TetrisPieceService {
    public List<Point> rotate(TetrisPiece tetrisPiece, TileMap tileMap) {
        int currentRotations = tetrisPiece.getRotationCounter();
        tetrisPiece.setRotationCounter(++currentRotations);
        List<Point> currentPoints = new ArrayList<>(tetrisPiece.getPoints());
        Point rotationCenter = calculateCenter(currentPoints);

        int minYBeforeRotation = currentPoints.stream().mapToInt(point -> (int) point.getY()).min().orElse(0);

        currentPoints.forEach(point -> {
            int relativeX = point.x - rotationCenter.x;
            int relativeY = point.y - rotationCenter.y;
            point.setLocation(rotationCenter.x + relativeY, rotationCenter.y - relativeX);
        });

        int minYAfterRotation = currentPoints.stream().mapToInt(point -> (int) point.getY()).min().orElse(0);
        int yAdjustment = minYBeforeRotation - minYAfterRotation;
        currentPoints.forEach(point -> point.translate(0, yAdjustment));

        int minX = currentPoints.stream().mapToInt(point -> (int) point.getX()).min().orElse(0);
        int maxX = currentPoints.stream().mapToInt(point -> (int) point.getX()).max().orElse(0);
        int xAdjustment;

        if (minX < 0) {
            xAdjustment = -minX;
        } else if (maxX >= tileMap.getWidth()) {
            xAdjustment = tileMap.getWidth() - 1 - maxX;
        } else {
            xAdjustment = 0;
        }

        currentPoints.forEach(point -> point.translate(xAdjustment, 0));

        return currentPoints;
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
