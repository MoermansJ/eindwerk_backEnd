package be.intecbrussel.eindwerk.games.tetris.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameStateRequest {
    private Boolean computerMove;
    private String keyPressed;
    private String sessionId;
    private String username;
}
