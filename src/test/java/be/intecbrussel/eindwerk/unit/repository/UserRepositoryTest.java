package be.intecbrussel.eindwerk.unit.repository;

import be.intecbrussel.eindwerk.model.User;
import be.intecbrussel.eindwerk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindUserByUsername_UserExists() {
        // Arrange
        User existingUser = new User("existingUser", "password");
        entityManager.persistAndFlush(existingUser);

        // Act
        Optional<User> foundUser = userRepository.findUserByUsername("existingUser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(existingUser.getUsername(), foundUser.get().getUsername());
        assertEquals(existingUser.getPassword(), foundUser.get().getPassword());
    }

    @Test
    void testFindUserByUsername_UserDoesNotExist() {
        // Act
        Optional<User> foundUser = userRepository.findUserByUsername("nonexistentUser");

        // Assert
        assertTrue(foundUser.isEmpty());
    }

    @Test
    void testFindUserByUsername_NullUsername() {
        // Act
        Optional<User> foundUser = userRepository.findUserByUsername(null);

        // Assert
        assertTrue(foundUser.isEmpty());
    }
}
