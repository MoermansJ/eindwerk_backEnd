package be.intecbrussel.eindwerk.games.tetris.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameStateRequest {
    private List<String> movementBuffer;
    private String sessionId;
    private String username;
    private String seed;
}
