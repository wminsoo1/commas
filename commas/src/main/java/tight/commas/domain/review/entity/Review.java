package tight.commas.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import tight.commas.domain.BaseTimeEntity;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.review.Tag;
import tight.commas.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id")
    private Park park;

    @Setter
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewTag> reviewTags = new ArrayList<>();

    @Setter
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @Setter
    private List<String> imageUrls = new ArrayList<>();

    @Setter
    private int starScore;

    public List<Tag> getTags() {
        return reviewTags.stream()
                .map(ReviewTag::getTag)
                .collect(Collectors.toList());
    }

    public void postReview(Park park, User user, String description, int starScore) {
        this.park = park;
        this.user = user;
        this.description = description;
        this.starScore = starScore;
    }

    public void postReviewWithImage(User user, Park park, String description, List<String> imageUrls, int starScore) {
        this.user = user;
        this.park = park;
        this.description = description;
        this.imageUrls = imageUrls;
        this.starScore = starScore;
    }
}
