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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Long score;


    public HighScore(String username, Long score) {
        this.username = username;
        this.score = score;
    }
}