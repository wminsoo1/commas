package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Drink extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "drink_id")
    private Long id;

    private Address address;
}
