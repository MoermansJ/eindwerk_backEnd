package be.intecbrussel.eindwerk.games.tetris.model.piece;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class PieceT extends TetrisPiece {
    @Id
    private Long id;

    private String content = "red";

    public PieceT() {
        super(new String[][]{
                {"_", "_", "_"},
                {"1", "1", "1"},
                {"_", "1", "_"}
        });
    }
}
