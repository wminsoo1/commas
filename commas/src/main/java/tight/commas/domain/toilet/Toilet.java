package tight.commas.domain.toilet;

import jakarta.persistence.*;
import lombok.Getter;
import tight.commas.domain.Address;
import tight.commas.domain.BaseEntity;
import tight.commas.domain.BaseTimeEntity;

@Entity
@Getter
public class Toilet extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "toilet_id")
    private Long id;

    private Address address;


}