package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.dto.AuthAttempt;
import be.intecbrussel.eindwerk.dto.LoginResponse;
import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.UserRepository;
import be.intecbrussel.eindwerk.security.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public User register(AuthAttempt authAttempt) {
        Optional<User> oDbUser = userRepository.findUserByUsername(authAttempt.getUsername());

        if (oDbUser.isPresent()) {
            throw new InvalidCredentialsException("That username is already registered.");
        }

        if (authAttempt.getPassword().length() <= 6) {
            throw new InvalidCredentialsException("Password must be at least 7 characters.");
        }

        return userRepository.save(new User(authAttempt.getUsername(), bCryptPasswordEncoder.encode(authAttempt.getPassword())));
    }

    public LoginResponse login(AuthAttempt authAttempt) {
        Optional<User> oDbUser = userRepository.findUserByUsername(authAttempt.getUsername());

        if (oDbUser.isEmpty()) {
            throw new InvalidCredentialsException("Username not found.");
        }

        User dbUser = oDbUser.get();

        if (!bCryptPasswordEncoder.matches(authAttempt.getPassword(), dbUser.getPassword())) {
            throw new InvalidCredentialsException("Incorrect password.");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authAttempt.getUsername(), authAttempt.getPassword()));
        String email = authentication.getName();
        User user = new User(email, "");
        String token = jwtUtil.createToken(user, "NO ROLES IN THIS APPLICATION YET");
        LoginResponse loginResponse = new LoginResponse(email, token);

        return loginResponse;
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateClaims(token);
    }
}
