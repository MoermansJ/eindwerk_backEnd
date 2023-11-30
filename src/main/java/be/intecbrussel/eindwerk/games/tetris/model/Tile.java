package be.intecbrussel.eindwerk.games.tetris.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.awt.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Tile {
    //properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Point point;
    private String content;

    public Tile(int x, int y, String content) {
        this.point = new Point(x, y);
        this.content = content;
    }
}
