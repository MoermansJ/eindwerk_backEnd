package be.intecbrussel.eindwerk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthAttempt {
    private String username;
    private String password;
}
