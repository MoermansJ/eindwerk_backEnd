package be.intecbrussel.eindwerk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponse {
    private String username;
    private String token;
}
