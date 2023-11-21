package be.intecbrussel.eindwerk.model.tetris;

import be.intecbrussel.eindwerk.model.tetris.piece.PieceZ;
import be.intecbrussel.eindwerk.model.tetris.piece.TetrisPiece;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_tilemap")
public class TileMap {
    //properties
    @Id
//    @Column(name = "session_id") //in case of emergency
    private long id;

    private int width;
    private int height;

    @OneToMany
    private List<Tile> tiles;

    @OneToOne
    private TetrisPiece currentPiece;


    //constructors
    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = this.createEmptyTileMap(width, height);
        this.currentPiece = new PieceZ();
    }

    //custom methods
    private List<Tile> createEmptyTileMap(int width, int height) {
        List<Tile> emptyTileMap = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < (height); y++) {
                emptyTileMap.add(new Tile(x, y, "| |"));
            }
        }

        return emptyTileMap;
    }

    public void insertPiece() {
        List<Tile> matchingTiles = new ArrayList<>();

        for (Point p : currentPiece.getPoints()) {
            List<Tile> matchingTilesForPoint = tiles.stream()
                    .filter(t -> t.getX() == p.x && t.getY() == p.y)
                    .collect(Collectors.toList());

            matchingTiles.addAll(matchingTilesForPoint);
        }

        matchingTiles.forEach(mt -> mt.setContent("P"));
    }
}
