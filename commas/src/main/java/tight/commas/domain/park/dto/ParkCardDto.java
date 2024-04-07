package tight.commas.domain.park.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import tight.commas.domain.Address;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.review.Tag;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.entity.ReviewTag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ParkCardDto {

    private String parkName;
    private Address address;
    private String imageUrl;
    private List<Tag> reviewTags;

    @QueryProjection
    public ParkCardDto(String parkName, Address address, String imageUrl) {
        this.parkName = parkName;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public ParkCardDto(String parkName, Address address, String imageUrl, List<ReviewTag> reviewTags) {
        this.parkName = parkName;
        this.address = address;
        this.imageUrl = imageUrl;
        this.reviewTags = reviewTags.stream()
                .map(ReviewTag::getTag)
                .collect(Collectors.toList());
    }
}
