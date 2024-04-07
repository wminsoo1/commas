package tight.commas.domain.park;

import lombok.Data;
import tight.commas.domain.review.Tag;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParkSearchCondition {
    private String parkName;
    private List<Tag> tags = new ArrayList<>();
}
