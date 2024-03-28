package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import tight.commas.domain.park.entity.Park;

import java.time.LocalDateTime;

@Entity
@Getter
public class Review extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    private String description;

    private String imageUrl;

    private int starScore;

    private LocalDateTime timeStamp;

    private String tag;
}
