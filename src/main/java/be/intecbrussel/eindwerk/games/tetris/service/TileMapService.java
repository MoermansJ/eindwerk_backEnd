package be.intecbrussel.eindwerk.games.tetris.service;

import be.intecbrussel.eindwerk.games.tetris.model.GameState;
import be.intecbrussel.eindwerk.games.tetris.model.Tile;
import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TileMapService {
    public List<Tile> createEmptyTileMap(int width, int height) {
        List<Tile> emptyTileMap = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < (height); y++) {
                emptyTileMap.add(new Tile(x, y, "white"));
            }
        }

        return emptyTileMap;
    }

    public void clearTetrisPiece(GameState gameState) {
        TetrisPiece tetrisPiece = gameState.getCurrentPiece();
        List<Tile> matchingTiles = this.getMatchingTiles(gameState);
        matchingTiles.forEach(mt -> mt.setContent("white"));
    }

    public void paintTetrisPiece(GameState gameState) {
        TetrisPiece tetrisPiece = gameState.getCurrentPiece();
        List<Tile> matchingTiles = this.getMatchingTiles(gameState);
        matchingTiles.forEach(mt -> mt.setContent(tetrisPiece.getContent()));
    }

    private List<Tile> getMatchingTiles(GameState gameState) {
        List<Tile> matchingTiles = new ArrayList<>();
        List<Tile> tiles = gameState.getTileMap().getTiles();
        TetrisPiece tetrisPiece = gameState.getCurrentPiece();

        for (Point p : tetrisPiece.getPoints()) {
            List<Tile> matchingTilesForPoint = tiles.stream()
                    .filter(t -> t.getX() == p.x && t.getY() == p.y)
                    .toList();

            matchingTiles.addAll(matchingTilesForPoint);
        }

        return matchingTiles;
    }
}
