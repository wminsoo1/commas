package tight.commas.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tight.commas.domain.review.entity.ReviewTag;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {
}
