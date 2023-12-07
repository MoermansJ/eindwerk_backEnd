package be.intecbrussel.eindwerk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthTokenDTO {
    private String username;
    private String token;
}
