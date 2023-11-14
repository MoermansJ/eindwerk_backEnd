package be.intecbrussel.eindwerk.unit.service;

import be.intecbrussel.eindwerk.dto.AuthAttempt;
import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.UserRepository;
import be.intecbrussel.eindwerk.service.AuthService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Disabled
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegister_SuccessfulRegistration() {
        // Arrange
        AuthAttempt authAttempt = new AuthAttempt("newUsername", "password");
        when(userRepository.findUserByUsername("newUsername")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User registeredUser = authService.register(authAttempt);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("newUsername", registeredUser.getUsername());
        assertEquals("password", registeredUser.getPassword());

        // Verify that userRepository methods were called
        verify(userRepository, times(1)).findUserByUsername("newUsername");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Arrange
        AuthAttempt authAttempt = new AuthAttempt("existingUsername", "password");
        when(userRepository.findUserByUsername("existingUsername")).thenReturn(Optional.of(new User("existingUsername", "password")));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authService.register(authAttempt));
        assertEquals("That username is already registered.", exception.getMessage());

        // Verify that userRepository methods were called
        verify(userRepository, times(1)).findUserByUsername("existingUsername");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_InvalidPassword() {
        // Arrange
        AuthAttempt authAttempt = new AuthAttempt("newUsername", "short");
        when(userRepository.findUserByUsername("newUsername")).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authService.register(authAttempt));
        assertEquals("Password must be at least 7 characters.", exception.getMessage());

        // Verify that userRepository methods were called
        verify(userRepository, times(1)).findUserByUsername("newUsername");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_SuccessfulLogin() {
        // Arrange
        AuthAttempt authAttempt = new AuthAttempt("existingUsername", "password");
        when(userRepository.findUserByUsername("existingUsername")).thenReturn(Optional.of(new User("existingUsername", "password")));

        // Act
        User loggedInUser = authService.login(authAttempt);

        // Assert
        assertNotNull(loggedInUser);
        assertEquals("existingUsername", loggedInUser.getUsername());
        assertEquals("password", loggedInUser.getPassword());

        // Verify that userRepository methods were called
        verify(userRepository, times(1)).findUserByUsername("existingUsername");
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        AuthAttempt authAttempt = new AuthAttempt("nonexistentUsername", "password");
        when(userRepository.findUserByUsername("nonexistentUsername")).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authService.login(authAttempt));
        assertEquals("Username not found.", exception.getMessage());

        // Verify that userRepository methods were called
        verify(userRepository, times(1)).findUserByUsername("nonexistentUsername");
    }

    @Test
    void testLogin_IncorrectPassword() {
        // Arrange
        AuthAttempt authAttempt = new AuthAttempt("existingUsername", "wrongPassword");
        when(userRepository.findUserByUsername("existingUsername")).thenReturn(Optional.of(new User("existingUsername", "password")));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authService.login(authAttempt));
        assertEquals("Incorrect password.", exception.getMessage());

        // Verify that userRepository methods were called
        verify(userRepository, times(1)).findUserByUsername("existingUsername");
    }
}
