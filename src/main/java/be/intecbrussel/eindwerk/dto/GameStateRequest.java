package be.intecbrussel.eindwerk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameStateRequest {
    private Boolean computerMove;
    private String key;
}
