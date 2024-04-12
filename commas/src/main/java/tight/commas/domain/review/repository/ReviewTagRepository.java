package tight.commas.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.entity.ReviewTag;

import java.util.List;


public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {

    void deleteAllByReview(Review review);

    @Query("SELECT rt FROM ReviewTag rt JOIN FETCH rt.review r WHERE r.park = :park")
    List<ReviewTag> findAllByPark(Park park);
}
