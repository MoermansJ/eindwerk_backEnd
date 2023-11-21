package be.intecbrussel.eindwerk.model.tetris;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    //constructors
    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = this.createEmptyTileMap(width, height);
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

//    public static void insertPiece(int x, int y) {
//        int pieceRows = piece.getShape().length;
//        int pieceCols = piece.getShape()[0].length;
//
//        for (int i = 0; i < pieceRows; i++) {
//            for (int j = 0; j < pieceCols; j++) {
//                tileMap[x + i][y + j] = piece.getShape()[i][j];
//            }
//        }
//    }
//
//    public static void resetGrid() {
//        tileMap = createEmptyTileMap();
//        firstGame = true;
//    }
}
