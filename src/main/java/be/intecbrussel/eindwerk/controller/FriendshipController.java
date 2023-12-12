package be.intecbrussel.eindwerk.controller;

import be.intecbrussel.eindwerk.model.Friendship;
import be.intecbrussel.eindwerk.service.FriendshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {
    private FriendshipService friendShipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendShipService = friendshipService;
    }

    @PostMapping("/addFriendship")
    public ResponseEntity addFriendship(@RequestBody Friendship friendship) {
        try {
            return ResponseEntity.ok(friendShipService.addFriendship(friendship));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
