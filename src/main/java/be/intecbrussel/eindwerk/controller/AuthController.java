package be.intecbrussel.eindwerk.controller;

import be.intecbrussel.eindwerk.dto.AuthAttemptDTO;
import be.intecbrussel.eindwerk.dto.AuthTokenDTO;
import be.intecbrussel.eindwerk.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthAttemptDTO authAttemptDTO) {
        try {
            return ResponseEntity.ok(authService.register(authAttemptDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed. " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthAttemptDTO authAttemptDTO) {
        try {
            return ResponseEntity.ok(authService.login(authAttemptDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Log in failed. " + e.getMessage());
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity validateToken(@RequestBody AuthTokenDTO authToken) {
        try {
            return ResponseEntity.ok(authService.validateToken(authToken.getToken()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Token validation failed. " + e.getMessage());
        }
    }

    @GetMapping("/getSessionId")
    public ResponseEntity getSessionId() {
        try {
            String sessionId = UUID.randomUUID().toString();
            Map<String, String> response = new HashMap<>(); // Making it JSON compatible
            response.put("sessionId", sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
