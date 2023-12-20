package be.intecbrussel.eindwerk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_highscore")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HighScore {
    private String username;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long score;

    public HighScore(String username, long score) {
        this.username = username;
        this.score = score;
    }
}