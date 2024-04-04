package tight.commas.domain.park.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkDto;

public interface ParkRepositoryCustom {
    Page<ParkDto> search(ParkSearchCondition condition, Pageable pageable);
}
