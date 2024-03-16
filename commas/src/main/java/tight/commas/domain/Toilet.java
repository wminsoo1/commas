package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Toilet {

    @Id @GeneratedValue
    @Column(name = "toilet_id")
    private Long id;

    private Address address;
}
