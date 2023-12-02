package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public abstract class TetrisPiece {
    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    protected List<Point> points;

    protected int rotationCounter;

    protected String content;


    // Constructors
    public TetrisPiece(List<Point> tiles) {
        this.points = tiles;
    }
}
