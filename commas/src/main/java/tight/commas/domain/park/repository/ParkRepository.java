package tight.commas.domain.park.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.entity.Park;

@Repository
public interface ParkRepository extends JpaRepository<Park, Long> {
    Park findByParkName(String parkName);
}
