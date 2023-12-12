package be.intecbrussel.eindwerk.repository.mysql;

import be.intecbrussel.eindwerk.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

}
