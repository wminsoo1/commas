package tight.commas.domain.park.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tight.commas.domain.park.entity.Park;


@Repository
public interface ParkRepository extends JpaRepository<Park, Long>, ParkRepositoryCustom {

    Park findByParkName(String parkName);
}
