package be.intecbrussel.eindwerk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_user")
public class User {
    @Column(unique = true)
    private String username;
    private String password;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
