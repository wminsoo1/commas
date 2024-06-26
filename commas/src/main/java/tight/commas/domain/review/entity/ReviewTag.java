package tight.commas.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import tight.commas.domain.review.Tag;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewTag {

    @Id @GeneratedValue
    @Column(name = "review_tag_id")
    private Long id;

    @Setter
    @Enumerated(value = EnumType.STRING)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @Setter
    private Review review;

    // 연관관계 편의 메서드
    public void addReview(Review review) {
        this.review = review;
        review.getReviewTags().add(this);
    }

    @Override
    public String toString() {
        return "ReviewTag{" +
                "id=" + id +
                ", tag=" + tag +
                ", review=" + review +
                '}';
    }
}
