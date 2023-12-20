package be.intecbrussel.eindwerk.games.tetris.model;

import be.intecbrussel.eindwerk.games.tetris.model.piece.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_gamestate")
public class GameState {
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> seededGenerationPattern;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TileMap currentPieceTileMap;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TileMap nextPieceTileMap;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TetrisPiece currentPiece;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private TetrisPiece nextPiece;
    private String sessionId;
    private String username;
    private String seed;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long timeStamp;
}

