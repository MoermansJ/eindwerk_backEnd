package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.TileMap;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import org.springframework.stereotype.Service;

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
        TetrisPiece currentPiece = gameState.getCurrentPiece();
        List<Tile> tileMapTiles = gameState.getTileMap().getTiles();

        this.getMatchingTiles(tileMapTiles, currentPiece)
                .forEach(tile -> tile.setContent("blank"));
    }

    public void paintCurrentPiece(GameState gameState) {
        TetrisPiece tetrisPiece = gameState.getCurrentPiece();
        List<Tile> matchingTiles = this.getMatchingTiles(gameState.getTileMap().getTiles(), gameState.getCurrentPiece());
        matchingTiles.forEach(tile -> tile.setContent(tetrisPiece.getContent()));
    }

    private List<Tile> getMatchingTiles(List<Tile> tileMapTiles, TetrisPiece tetrisPiece) {
        return tetrisPiece.getPoints().stream()
                .flatMap(piecePoint -> tileMapTiles.stream()
                        .filter(tile -> tile.getPoint().equals(piecePoint)))
                .collect(Collectors.toList());
    }

    public void removeCompletedLinesFromTileMap(TileMap tileMap) {
        int width = tileMap.getWidth();
        int height = tileMap.getHeight();
        List<Integer> completedRowIndexes = new ArrayList<>();

        boolean linesRemoved = false;
        do {
            linesRemoved = false;

            // Scanning all rows to see if they're completed
            for (int rowIndex = 0; rowIndex < height; rowIndex++) {
                List<Tile> currentRowTiles = this.getTilesByRow(tileMap, rowIndex);
                boolean isRowComplete = currentRowTiles.size() == width;

                if (isRowComplete) {
                    completedRowIndexes.add(rowIndex);
                    linesRemoved = true;
                }
            }

            if (linesRemoved) {
                // Remove completed rows and insert blank rows
                for (int i = completedRowIndexes.size() - 1; i >= 0; i--) {
                    int rowIndex = completedRowIndexes.get(i);

                    tileMap.getTiles().removeAll(this.getTilesByRow(tileMap, rowIndex));
                    this.collapseRows(tileMap, rowIndex);
                    this.updateLinesCleared(tileMap);
                    tileMap.getTiles().addAll(this.getNewTopRow(tileMap));
                }

                completedRowIndexes.clear(); // Clear the list for the next iteration
            }
        } while (linesRemoved);
    }


    private List<Tile> getTilesByRow(TileMap tileMap, int rowIndex) {
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
        // For each row that is between the completed row and, including, top row
        for (int i = (removedRowIndex + 1); i < tileMap.getHeight(); i++) {
            final int currentRow = i;
            tileMap.getTiles().stream()
                    .filter(tile -> tile.getPoint().y == currentRow)
                    .forEach(tile -> {
                        int x = tile.getPoint().x;
                        int y = tile.getPoint().y - 1;
                        tile.getPoint().setLocation(x, y);
                    });
        }
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
