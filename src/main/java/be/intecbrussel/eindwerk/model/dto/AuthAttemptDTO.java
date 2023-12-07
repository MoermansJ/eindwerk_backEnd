package be.intecbrussel.eindwerk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthAttemptDTO {
    private String username;
    private String password;
}
