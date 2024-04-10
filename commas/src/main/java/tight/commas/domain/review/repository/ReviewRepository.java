package tight.commas.domain.review.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tight.commas.domain.review.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByParkId(Long parkId);

//    @EntityGraph(attributePaths = {"park"})  //fetch join annotation
    @Query("select r from Review r join fetch r.park where r.park.id = :parkId")
    List<Review> findFetchJoinAllByParkId(Long parkId);
}
