package tight.commas.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewPostDto {
    private String description;
    private int starScore;
    private List<String> reviewTags;
}
