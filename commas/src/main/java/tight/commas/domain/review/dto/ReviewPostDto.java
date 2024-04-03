package tight.commas.domain.review.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewPostDto {
    private String description;
    private int starScore;
    private List<String> reviewTags;
}
