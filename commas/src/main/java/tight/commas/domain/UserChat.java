package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class UserChat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    private String description;
}
