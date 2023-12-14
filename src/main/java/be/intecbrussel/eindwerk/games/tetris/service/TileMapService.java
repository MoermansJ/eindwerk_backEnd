package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TileMapService {
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
        TetrisPiece tetrisPiece = gameState.getCurrentPiece();
        List<Tile> matchingTiles = this.getMatchingTiles(gameState.getTileMap().getTiles(), gameState.getCurrentPiece());
        matchingTiles.forEach(mt -> mt.setContent("blank"));
    }

    public void paintCurrentPiece(GameState gameState) {
        TetrisPiece tetrisPiece = gameState.getCurrentPiece();
        List<Tile> matchingTiles = this.getMatchingTiles(gameState.getTileMap().getTiles(), gameState.getCurrentPiece());
        matchingTiles.forEach(mt -> mt.setContent(tetrisPiece.getContent()));
    }

    private List<Tile> getMatchingTiles(List<Tile> tileMapTiles, TetrisPiece tetrisPiece) {
        List<Tile> matchingTiles = new ArrayList<>();

        for (Point p : tetrisPiece.getPoints()) {
            List<Tile> matchingTilesForPoint = tileMapTiles.stream()
                    .filter(tmt -> tmt.getPoint().equals(p))
                    .collect(Collectors.toList());

            matchingTiles.addAll(matchingTilesForPoint);
        }

        return matchingTiles;
    }

    public void removeCompleteLinesFromTileMap(TileMap tileMap) {
        int width = tileMap.getWidth();
        int height = tileMap.getHeight();

        // Scanning all rows to see if they're completed
        for (int rowIndex = 0; rowIndex < height; rowIndex++) {
            List<Tile> currentRowNoBlankTiles = this.getNonBlankTilesByRow(tileMap, rowIndex);
            boolean isRowComplete = currentRowNoBlankTiles.size() == tileMap.getWidth();

            if (!isRowComplete) {
                continue;
            }

            // Removing tiles from completed row
            tileMap.getTiles().removeAll(currentRowNoBlankTiles);

            // Moving all the rows above the removed row down by one
            this.collapseRows(tileMap, rowIndex);

            // Adding a new row of blank tiles as the new top row
            tileMap.getTiles().addAll(this.getNewTopRow(tileMap));

            // Update users score
            this.updateLinesCleared(tileMap);
        }

    }

    private List<Tile> getNonBlankTilesByRow(TileMap tileMap, int rowIndex) {
        return tileMap.getTiles().stream()
                .filter(tile -> tile.getPoint().getY() == rowIndex)
                .filter(tile -> !tile.getContent().equals("blank"))
                .collect(Collectors.toList());
    }


    private List<Tile> getNewTopRow(TileMap tileMap) {
        List<Tile> blankRowTileList = new ArrayList<>();
        int width = tileMap.getWidth();
        int y = tileMap.getHeight() - 1;

        for (int x = 0; x < width; x++) {
            blankRowTileList.add(new Tile(x, y, "blank"));
        }

        return blankRowTileList;
    }

    private void collapseRows(TileMap tileMap, int removedRowIndex) {
        List<Tile> updatedTiles = new ArrayList<>();

        // For each row that is between the completed row and, including, top row
        for (int i = (removedRowIndex + 1); i < tileMap.getHeight(); i++) {
            final int currentRow = i;
            tileMap.getTiles().stream()
                    .filter(tile -> tile.getPoint().y == currentRow)
                    .forEach(tile -> {
                        int x = tile.getPoint().x;
                        int y = tile.getPoint().y - 1;
                        updatedTiles.add(new Tile(x, y, tile.getContent()));
                    });
        }

        tileMap.setTiles(updatedTiles);
    }

    public void positionNewTetrisPiece(GameState gameState) {
        TileMap tileMap = gameState.getTileMap();
        int offsetX = (tileMap.getWidth() / 2);
        int offsetY = tileMap.getHeight() - 2;

        TetrisPiece tetrisPiece = gameState.getCurrentPiece();
        tetrisPiece.getPoints().forEach(point -> {
            int x = (int) (point.getX() + offsetX);
            int y = (int) (point.getY() + offsetY);
            point.setLocation(x, y);
        });
    }

    private void updateLinesCleared(TileMap tileMap) {
        int linesCleared = tileMap.getLinesCleared();
        tileMap.setLinesCleared(++linesCleared);
    }
}
