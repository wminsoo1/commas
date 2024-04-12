package tight.commas.domain.review;

import lombok.Getter;

@Getter
public enum Tag {
    WALK("걷기가 좋은"),
    PRETTY("풍경이 예쁜"),
    PICNIC("소풍하기 좋은"),
    BIKE_TRAIL("자전거 트레일이 있는"),
    DOG_FRIENDLY("반려견 동반 가능한"),
    RIVERSIDE("강가의"),
    URBAN("도심 속"),
    NIGHT_VIEW("야경"),
    CULTURAL("문화 예술의"),
    NATURAL_RELAX("자연 휴식"),
    FAMILY_OUTING("가족 나들이"),
    PLAYGROUND("놀이터"),
    SPORT("스포츠"),
    HISTORY("역사의"),
    EDUCAtiON("교육")
    ;

    private final String description;

    Tag(String description) {
        this.description = description;
    }

    public static Tag findByDescription(String description) {
        for (Tag tag : Tag.values()) {
            if (tag.getDescription().equals(description)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 리뷰 태그입니다.");
    }
}
