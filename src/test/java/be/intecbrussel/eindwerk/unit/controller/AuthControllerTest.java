package be.intecbrussel.eindwerk.unit.controller;

import be.intecbrussel.eindwerk.controller.AuthController;
import be.intecbrussel.eindwerk.dto.AuthAttemptDTO;
import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.UserRepository;
import be.intecbrussel.eindwerk.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Disabled
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        // Mocking the registration behavior in AuthService
        User mockUser = new User("username", "password");
        when(authService.register(any(AuthAttemptDTO.class))).thenReturn(mockUser);

        // Creating a sample AuthAttempt
        AuthAttemptDTO authAttemptDTO = new AuthAttemptDTO("username", "password");

        // Performing the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .content(asJsonString(authAttemptDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRegisterFailure() throws Exception {
        // Mocking the registration behavior in AuthService to throw an exception
        when(authService.register(any(AuthAttemptDTO.class))).thenThrow(new RuntimeException("Registration failed."));

        // Creating a sample AuthAttempt
        AuthAttemptDTO authAttemptDTO = new AuthAttemptDTO("username", "password");

        // Performing the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .content(asJsonString(authAttemptDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Mocking the login behavior in AuthService
        User mockUser = new User("username", "password");
        when(userRepository.findUserByUsername("username")).thenReturn(Optional.of(mockUser));

        // Creating a sample AuthAttempt
        AuthAttemptDTO authAttemptDTO = new AuthAttemptDTO("username", "password");

        // Performing the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(asJsonString(authAttemptDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testLoginFailureUserNotFound() throws Exception {
        // Mocking the login behavior in AuthService to throw an EntityNotFoundException
        when(userRepository.findUserByUsername("username")).thenReturn(Optional.empty());
        when(authService.login(any(AuthAttemptDTO.class))).thenThrow(new RuntimeException("Log in failed."));


        // Creating a sample AuthAttempt
        AuthAttemptDTO authAttemptDTO = new AuthAttemptDTO("username", "password");

        // Performing the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .content(asJsonString(authAttemptDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    // Helper method to convert objects to JSON
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
