package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Chat extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    private String chatname;
}
