package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private String email;
}
