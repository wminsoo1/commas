package tight.commas.domain.review.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.repository.ParkRepository;
import tight.commas.domain.review.Tag;
import tight.commas.domain.review.dto.ReviewDto;
import tight.commas.domain.review.dto.ReviewPostDto;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.entity.ReviewTag;
import tight.commas.domain.review.repository.ReviewRepository;
import tight.commas.domain.review.repository.ReviewTagRepository;
import tight.commas.domain.user.dto.UserDto;
import tight.commas.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewTagRepository reviewTagRepository;
    private final ParkRepository parkRepository;
    private final EntityManager em;

    public List<ReviewDto> getReview(Long parkId) {

        List<Review> reviews = reviewRepository.findALlByParkId(parkId);

        return reviews.stream().map(
                review -> ReviewDto.builder()
                        .user(UserDto.Response.fromUser(review.getUser()))
                        .description(review.getDescription())
                        .reviewTags(review.getReviewTags().stream().map(reviewTag -> reviewTag.getTag().getDescription()).toList())
                        .starScore(review.getStarScore())
                        .timestamp(review.getCreatedDate())
                        .parkId(review.getPark().getId())
                        .build()
                ).toList();
    }

    public void postReview(Long parkId, User user, ReviewPostDto reviewDto) {

        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("해당 장소가 존재하지 않습니다."));


        Review review = Review.builder()
                .park(park)
                .user(user)
                .description(reviewDto.getDescription())
                .starScore(reviewDto.getStarScore())
                .build();

        List<ReviewTag> reviewTags = reviewDto.getReviewTags().stream()
                .map(tag -> {
                    ReviewTag reviewTag = new ReviewTag();
                    reviewTag.setTag(Tag.valueOf(tag));

                    reviewTag.addReview(review);

                    return reviewTag;
                }).toList();

        review.setReviewTags(reviewTags);

        reviewTagRepository.saveAll(reviewTags);
        reviewRepository.save(review);
    }

    public void postReviewV2(Long parkId, User user, ReviewPostDto reviewDto) {

        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("해당 장소가 존재하지 않습니다."));
        Review review = new Review(user, park, reviewDto.getDescription(), reviewDto.getStarScore());

        em.persist(review);
        List<ReviewTag> reviewTags = new ArrayList<>();
        for (String tag : reviewDto.getReviewTags()) {
            ReviewTag reviewTag = new ReviewTag();
            em.persist(reviewTag);
            reviewTag.setTag(Tag.valueOf(tag));
            reviewTag.addReview(review);
            reviewTags.add(reviewTag);
        }

    }
}

