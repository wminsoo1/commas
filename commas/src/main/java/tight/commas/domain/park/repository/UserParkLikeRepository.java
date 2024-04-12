package tight.commas.domain.park.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.entity.UserParkLike;
import tight.commas.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserParkLikeRepository extends JpaRepository<UserParkLike, Long> {

    Optional<UserParkLike> findAllByUserAndPark(User user, Park park);

    List<UserParkLike> findAllByUserId(Long userId);

    @Query("SELECT upl FROM UserParkLike upl WHERE upl.user.id = :userId")
    List<UserParkLike> findByUserId(Long userId);
}
