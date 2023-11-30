package be.intecbrussel.eindwerk.games.tetris.model;

import be.intecbrussel.eindwerk.games.tetris.model.piece.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_gamestate")
public class GameState {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    @OneToOne(cascade = CascadeType.ALL)
    private TileMap tileMap;

    @OneToOne(cascade = CascadeType.ALL)
    private TetrisPiece currentPiece;

    private Long timeStamp;

    private int userScore;
}

