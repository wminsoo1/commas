package tight.commas.domain.park.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tight.commas.domain.BaseTimeEntity;
import tight.commas.domain.user.entity.User;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserParkLike extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_park_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Park park;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Boolean likeStatus;
}