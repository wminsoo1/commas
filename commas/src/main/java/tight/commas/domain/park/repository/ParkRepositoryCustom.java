package tight.commas.domain.park.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkCardDto;
import tight.commas.domain.park.dto.ParkCardDtoV2;

import java.util.List;

public interface ParkRepositoryCustom {
    Page<ParkCardDto> parkCardSearch(ParkSearchCondition condition, Pageable pageable);
    Page<ParkCardDtoV2> parkCardSearchV2(ParkSearchCondition condition, Pageable pageable);
    Page<ParkCardDtoV2> parkCardSearchV3(ParkSearchCondition condition, Pageable pageable);
    Page<ParkCardDtoV2> parkCardSearchV4(ParkSearchCondition condition, Pageable pageable);

    Page<ParkCardDtoV2> getParkCardDtoV2(Pageable pageable);

    List<Long> example(ParkSearchCondition condition);
}
