package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Mart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mart_id")
    private Long id;

    private Address address;
}
