package tight.commas.domain.review.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewPostWithImageDto {
    private String description;
    private int starScore;
    private List<MultipartFile> files;
    private List<String> reviewTags;
}
