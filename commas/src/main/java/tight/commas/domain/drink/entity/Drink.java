package tight.commas.domain.drink.entity;

import jakarta.persistence.*;
import lombok.Getter;
import tight.commas.domain.Address;
import tight.commas.domain.BaseEntity;
import tight.commas.domain.BaseTimeEntity;

import java.util.Map;

@Entity
@Getter
public class Drink extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "drink_id")
    private Long id;

    private Address address;


}