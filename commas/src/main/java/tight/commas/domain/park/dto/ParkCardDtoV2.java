package tight.commas.domain.park.dto;

import lombok.Data;
import tight.commas.domain.Address;
import tight.commas.domain.review.Tag;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.entity.ReviewTag;

import java.util.List;

@Data
public class ParkCardDtoV2 {

    private String parkName;
    private Address address;
    private String imageUrl;
    private List<Tag> reviewTags;

    public ParkCardDtoV2(Review review) {
        this.parkName = review.getPark().getParkName();
        this.address = review.getPark().getAddress();
        this.imageUrl = review.getPark().getImageUrl();
        this.reviewTags = review.getReviewTags().stream().map(ReviewTag::getTag).toList();
    }
}
