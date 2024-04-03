package tight.commas.domain.review;

import lombok.Getter;

@Getter
public enum Tag {
    WALK("걷기가 좋은"),
    PRETTY("풍경이 예쁜"),
    PICNIC("소풍하기 좋은"),
    BIKE_TRAIL("자전거 트레일이 있는"),
    DOG_FRIENDLY("반려견 동반 가능한");

    private final String description;

    Tag(String description) {
        this.description = description;
    }
}
