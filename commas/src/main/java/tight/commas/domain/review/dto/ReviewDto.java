package tight.commas.domain.review.dto;

import lombok.Builder;
import lombok.Getter;
import tight.commas.domain.review.entity.ReviewTag;
import tight.commas.domain.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ReviewDto {

    private UserDto.Response user;
    private Long parkId;
    private Long reviewId;
    private String imageUrl;
    private String description;
    private int starScore;
    private LocalDateTime timestamp;
    private List<String> reviewTags;
}
