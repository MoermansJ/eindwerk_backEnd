package be.intecbrussel.eindwerk.games.tetris.model;

import be.intecbrussel.eindwerk.games.tetris.model.piece.TetrisPiece;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_tilemap")
public class TileMap {
    //properties
//    @Column(name = "session_id") //in case of emergency
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int width;
    private int height;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tile> tiles;

    private int linesCleared;

//    @OneToOne(cascade = CascadeType.ALL)
//    private TetrisPiece currentPiece;


    //constructors
//    public TileMap(int width, int height, List<Tile> tiles, TetrisPiece tetrisPiece) {
//        this.width = width;
//        this.height = height;
//        this.tiles = tiles;
//        this.currentPiece = tetrisPiece;
//    }
    public TileMap(int width, int height, List<Tile> tiles) {
        this.width = width;
        this.height = height;
        this.tiles = tiles;
    }
}
