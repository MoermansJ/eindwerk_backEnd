package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import be.intecbrussel.eindwerk.util.sorting.IntegerSorter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TileMapService {
    private IntegerSorter integerSorter;

    public TileMapService(IntegerSorter integerSorter) {
        this.integerSorter = integerSorter;
    }

    public List<Tile> createEmptyTileMap(int width, int height) {
        List<Tile> emptyTileMap = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < (height); y++) {
                emptyTileMap.add(new Tile(x, y, "blank"));
            }
        }

        return emptyTileMap;
    }

    public void unpaintCurrentPiece(GameState gameState) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        List<Tile> allTiles = gameState.getTileMap().getTiles();

        this.getMatchingTiles(allTiles, currentPiece)
                .forEach(tile -> tile.setContent("blank"));
    }

    public void paintCurrentPiece(GameState gameState) {
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        List<Tile> allTiles = gameState.getTileMap().getTiles();

        this.getMatchingTiles(allTiles, currentPiece)
                .forEach(tile -> tile.setContent(currentPiece.getContent()));
    }

    public List<Tile> getMatchingTiles(List<Tile> tileMapTiles, TetrisPiece tetrisPiece) {
        return tetrisPiece.getPoints().stream()
                .flatMap(piecePoint -> tileMapTiles.stream()
                        .filter(tile -> tile.getPoint().equals(piecePoint)))
                .collect(Collectors.toList());
    }

    public void removeCompletedRowsFromTileMap(TileMap tileMap) {
        List<Integer> completedRowIndexes = this.getCompletedRowIndexes(tileMap);

        this.integerSorter.sortHighToLow(completedRowIndexes)
                .forEach(rowIndex -> this.handleCompletedRow(tileMap, rowIndex));
    }

    private void handleCompletedRow(TileMap tileMap, int rowIndex) {
        List<Tile> allTiles = tileMap.getTiles();
        List<Tile> completedRowTiles = this.getTilesByRow(tileMap, rowIndex);
        List<Tile> newTopRowTiles = this.getNewTopRow(tileMap);

        allTiles.removeAll(completedRowTiles);
        collapseRows(tileMap, rowIndex);
        allTiles.addAll(newTopRowTiles);
        updateScoreCounter(tileMap);
    }

    private List<Integer> getCompletedRowIndexes(TileMap tileMap) {
        return IntStream
                .range(0, tileMap.getHeight())
                .filter(rowIndex -> this.isThisRowComplete(tileMap, rowIndex))
                .boxed()
                .collect(Collectors.toList());
    }

    private boolean isThisRowComplete(TileMap tileMap, int rowIndex) {
        List<Tile> rowTiles = this.getTilesByRow(tileMap, rowIndex);
        return rowTiles.size() == tileMap.getWidth();
    }


    private List<Tile> getTilesByRow(TileMap tileMap, int rowIndex) {
        return tileMap.getTiles().stream()
                .filter(tile -> tile.getPoint().getY() == rowIndex)
                .filter(tile -> !tile.getContent().equals("blank"))
                .collect(Collectors.toList());
    }

    private List<Tile> getNewTopRow(TileMap tileMap) {
        int width = tileMap.getWidth();
        int y = tileMap.getHeight() - 1;

        return IntStream
                .range(0, width)
                .mapToObj(x -> new Tile(x, y, "blank"))
                .collect(Collectors.toList());
    }

    private void collapseRows(TileMap tileMap, int removedRowIndex) {
        int rowAboveRemovedRowIndex = removedRowIndex + 1;
        List<Tile> allTiles = tileMap.getTiles();

        allTiles.stream()
                .filter(tile -> tile.getPoint().y >= rowAboveRemovedRowIndex)
                .forEach(tile -> {
                    Point point = tile.getPoint();
                    point.setLocation(point.x, point.y - 1);
                });
    }

    public void positionNewTetrisPiece(GameState gameState) {
        List<Point> newPiecePoints = gameState.getCurrentPiece().getPoints();
        int width = gameState.getTileMap().getWidth();
        int height = gameState.getTileMap().getHeight();
        int offsetX = width / 2;
        int offsetY = height - 3;

        newPiecePoints.forEach(point ->
                point.setLocation(
                        (point.getX() + offsetX),
                        (point.getY() + offsetY)
                )
        );
    }

    private void updateScoreCounter(TileMap tileMap) {
        int scoreCounter = tileMap.getLinesCleared();
        tileMap.setLinesCleared(++scoreCounter);
    }
}