package be.intecbrussel.eindwerk.games.tetris.model;

import be.intecbrussel.eindwerk.games.tetris.model.piece.*;
import be.intecbrussel.eindwerk.games.tetris.service.GameStateService;
import be.intecbrussel.eindwerk.service.GameService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.Point;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
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

    private LocalDateTime timeStamp;

    public enum Direction {
        LEFT, RIGHT, DOWN
    }


    //constructors
    public GameState() {
        this.tileMap = new TileMap();
        this.timeStamp = LocalDateTime.now();
    }
}

