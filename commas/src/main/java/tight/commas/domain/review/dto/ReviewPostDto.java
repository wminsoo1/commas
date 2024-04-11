package tight.commas.domain.review.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ReviewPostDto {
    private String description;
    private int starScore;
    private List<MultipartFile> files;
    private List<String> reviewTags;
}
