package tight.commas.domain.park.dto;

import lombok.Data;
import tight.commas.domain.review.entity.Review;

@Data
public class ParkReviewDescriptionDto {

    private Long reviewId;

    private String description;

    public ParkReviewDescriptionDto(Review review) {
        this.reviewId = review.getId();
        this.description = review.getDescription();
    }
}
