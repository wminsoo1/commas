package tight.commas.domain.review.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
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

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewTag> reviewTags = new ArrayList<>();

    private String description;

    private String imageUrl;

    private int starScore;

    public List<Tag> getTags() {
        return reviewTags.stream()
                .map(ReviewTag::getTag)
                .collect(Collectors.toList());
    }

    public void setReviewTags(List<ReviewTag> reviewTags) {
        this.reviewTags = reviewTags;
    }

    public Review(User user, Park park, String description, int starScore) {
        this.user = user;
        this.park = park;
        this.description = description;
        this.starScore = starScore;
    }
}
