package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.model.HighScore;
import be.intecbrussel.eindwerk.model.dto.AuthAttemptDTO;
import be.intecbrussel.eindwerk.model.dto.AuthTokenDTO;
import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.security.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;
    private HighScoreService highScoreService;


    public AuthService(
            UserService userService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil,
            HighScoreService highScoreService) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.highScoreService = highScoreService;
    }


    public User register(AuthAttemptDTO authAttemptDTO) {
        Optional<User> oDbUser = userService.findUserByUsername(authAttemptDTO.getUsername());

        if (oDbUser.isPresent()) {
            throw new InvalidCredentialsException("That username is already registered.");
        }

        if (authAttemptDTO.getPassword().length() <= 6) {
            throw new InvalidCredentialsException("Password must be at least 7 characters.");
        }

        return this.saveUserAndInitialiseHighScore(authAttemptDTO);
    }

    public AuthTokenDTO login(AuthAttemptDTO authAttemptDTO) {
        Optional<User> oDbUser = userService.findUserByUsername(authAttemptDTO.getUsername());

        if (oDbUser.isEmpty()) {
            throw new InvalidCredentialsException("Username not found.");
        }

        User dbUser = oDbUser.get();

        if (!bCryptPasswordEncoder.matches(authAttemptDTO.getPassword(), dbUser.getPassword())) {
            throw new InvalidCredentialsException("Incorrect password.");
        }

        return this.createAuthToken(authAttemptDTO);
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateClaims(token);
    }

    private AuthTokenDTO createAuthToken(AuthAttemptDTO authAttemptDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authAttemptDTO.getUsername(), authAttemptDTO.getPassword()));
        String username = authentication.getName();
        User user = new User(username, "");
        String token = jwtUtil.createToken(user, "NO ROLES IN THIS APPLICATION YET");

        return new AuthTokenDTO(username, token);
    }

    private User saveUserAndInitialiseHighScore(AuthAttemptDTO authAttemptDTO) {
        String username = authAttemptDTO.getUsername();
        String password = bCryptPasswordEncoder.encode(authAttemptDTO.getPassword());
        HighScore zero = new HighScore(username, 0);
        User user = new User(username, password);
        highScoreService.saveHighScore(zero);
        return userService.saveUser(user);
    }

    public Map<String, String> generateSessionId() {
        String sessionId = UUID.randomUUID().toString();
        Map<String, String> responseJson = new HashMap<>(); // Making it JSON compatible
        responseJson.put("sessionId", sessionId);
        return responseJson;
    }
}
