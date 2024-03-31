package tight.commas.domain.park.entity;

import jakarta.persistence.*;
import lombok.Getter;
import tight.commas.domain.park.Tag;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ParkTag {

    @Id @GeneratedValue
    @Column(name = "park_tag_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    public ParkTag() {
    }

    //연관관계 편의 메서드
    public void addPark(Park park) {
        this.park = park;
        park.getParkTags().add(this);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
