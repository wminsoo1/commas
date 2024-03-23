package tight.commas.domain.park.entity;

import jakarta.persistence.*;
import lombok.Getter;
import tight.commas.domain.Address;
import tight.commas.domain.BaseTimeEntity;
import tight.commas.domain.Chat;
import tight.commas.domain.User;

@Entity
@Getter
public class Park extends BaseTimeEntity {

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

    private int phoneNumber;

    private String plant; //주요 식물

    private String outLine; //개요

    private String tag;

    private Boolean likeStatus;
}
