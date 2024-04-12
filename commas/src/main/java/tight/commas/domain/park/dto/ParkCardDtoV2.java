package tight.commas.domain.park.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tight.commas.domain.Address;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.review.Tag;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.entity.ReviewTag;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParkCardDtoV2 {

    private Long parkId;
    private String parkName;
    private Address address;
    private String imageUrl;
    private Boolean likeStatus;
    private List<Tag> reviewTags;

    public ParkCardDtoV2(Review review) {
        this.parkName = review.getPark().getParkName();
        this.address = review.getPark().getAddress();
        this.imageUrl = review.getPark().getImageUrl();
        this.reviewTags = review.getReviewTags().stream().map(ReviewTag::getTag).toList();
    }

    public ParkCardDtoV2(Park park, List<Tag> tags) {
        this.parkId = park.getId();
        this.parkName = park.getParkName();
        this.address = park.getAddress();
        this.imageUrl = park.getImageUrl();
        this.reviewTags = tags;
    }

    @QueryProjection
    public ParkCardDtoV2(Long parkId, String parkName, Address address, String imageUrl) {
        this.parkId = parkId;
        this.parkName = parkName;
        this.address = address;
        this.imageUrl = imageUrl;
    }
}
