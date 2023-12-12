package be.intecbrussel.eindwerk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_friendship")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usernameA;
    private String usernameB;
}
