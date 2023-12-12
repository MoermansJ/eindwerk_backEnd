package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.exception.InvalidCredentialsException;
import be.intecbrussel.eindwerk.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SearchService {
    private UserService userService;

    public SearchService(UserService userService) {
        this.userService = userService;
    }

    public User searchUserByUsername(String username) {
        Optional<User> oDbUser = userService.findUserByUsername(username);

        if (oDbUser.isEmpty()) {
            throw new InvalidCredentialsException("That username isn't registered.");
        }

        return oDbUser.get();
    }


}
