package be.intecbrussel.eindwerk.service;

import be.intecbrussel.eindwerk.model.Friendship;
import be.intecbrussel.eindwerk.repository.mysql.FriendshipRepository;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService {
    private FriendshipRepository friendshipRepository;

    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public Friendship addFriendship(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }
}
