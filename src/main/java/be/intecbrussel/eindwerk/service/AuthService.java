package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.dto.AuthAttempt;
import be.intecbrussel.eindwerk.exception.InvalidPasswordException;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(AuthAttempt authAttempt) {
        Optional<User> oDbUser = userRepository.findUserByUsername(authAttempt.getUsername());

        if (oDbUser.isPresent()) {
            throw new EntityExistsException("That username is already registered.");
        }

        if (authAttempt.getPassword().length() <= 6) {
            throw new InvalidPasswordException("Password must be at least 7 characters.");
        }

        return userRepository.save(new User(authAttempt.getUsername(), authAttempt.getPassword()));
    }

    public User login(AuthAttempt authAttempt) {
        Optional<User> oDbUser = userRepository.findUserByUsername(authAttempt.getUsername());

        if (oDbUser.isEmpty()) {
            throw new EntityNotFoundException("Username not found.");
        }

        User dbUser = oDbUser.get();

        if (!dbUser.getPassword().equals(authAttempt.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }

        return dbUser;
    }
}
