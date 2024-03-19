package tight.commas.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Park extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "park_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Embedded
    private Address address;

    private String imageUrl;

    private int openTime;
    private int endTime;

    private int phoneNumber;

    private String plant; //주요 식물

    private String outLine; //개요

    private String tag;

    private Boolean likeStatus;
}
