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
import tight.commas.global.s3.AwsS3Service;

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
    private final AwsS3Service awsS3Service;

    public List<ReviewDto> findByParkId(Long parkId) {

        List<Review> reviews = reviewRepository.findAllByParkId(parkId);

        return reviews.stream().map(
                review -> ReviewDto.builder()
                        .user(UserDto.Response.fromUser(review.getUser()))
                        .description(review.getDescription())
                        .reviewTags(review.getReviewTags().stream().map(reviewTag -> reviewTag.getTag().getDescription()).toList())
                        .starScore(review.getStarScore())
                        .imageUrl(review.getImageUrls())
                        .timestamp(review.getCreatedDate())
                        .parkId(review.getPark().getId())
                        .reviewId(review.getId())
                        .build()
                ).toList();
    }

    public void postReview(Long parkId, User user, ReviewPostDto reviewPostDto) {

        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("해당 장소가 존재하지 않습니다."));


        Review review = Review.builder()
                .park(park)
                .user(user)
                .description(reviewPostDto.getDescription())
                .starScore(reviewPostDto.getStarScore())
                .build();

        List<ReviewTag> reviewTags = reviewPostDto.getReviewTags().stream()
                .map(description -> {
                    ReviewTag reviewTag = new ReviewTag();
                    reviewTag.setTag(Tag.findByDescription(description));

                    reviewTag.addReview(review);

                    return reviewTag;
                }).toList();

        review.setReviewTags(reviewTags);

        reviewTagRepository.saveAll(reviewTags);
        reviewRepository.save(review);
    }

    public void postReviewV2(Long parkId, User user, ReviewPostDto reviewPostDto) {

        Park park = parkRepository.findById(parkId)
                .orElseThrow(() -> new RuntimeException("해당 장소가 존재하지 않습니다."));

        List<String> imageUrls;
        if (reviewPostDto.getFiles() == null) {
            imageUrls = null;
        }else {
            imageUrls = awsS3Service.uploadFile(reviewPostDto.getFiles());
        }
        Review review = new Review();
        em.persist(review);
//        for (String imageUrl : imageUrls) {
//            review.getImageUrls().add(imageUrl);
//        }
        review.postReview(user, park, reviewPostDto.getDescription(),imageUrls, reviewPostDto.getStarScore());
        System.out.println("review = " + review.getImageUrls());

        List<ReviewTag> reviewTags = new ArrayList<>();

        for (String description : reviewPostDto.getReviewTags()) {
            ReviewTag reviewTag = new ReviewTag();
            em.persist(reviewTag);
            reviewTag.setTag(Tag.findByDescription(description));
            reviewTag.addReview(review);
            reviewTags.add(reviewTag);
        }
    }

    public void deleteReview(Long reviewId, User user) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 존재하지 않습니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("리뷰를 삭제할 수 있는 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }

    public void modifyReview(Long reviewId, User user, ReviewPostDto updateForm) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("해당 리뷰가 존재하지 않습니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("리뷰를 수정할 수 있는 권한이 없습니다.");
        }
        reviewTagRepository.deleteAllByReview(review);

        List<ReviewTag> reviewTags = new ArrayList<>();

        for (String description : updateForm.getReviewTags()) {
            ReviewTag reviewTag = new ReviewTag();
            reviewTag.setReview(review);
            reviewTag.setTag(Tag.findByDescription(description));
            reviewTags.add(reviewTag);
        }

        review.setReviewTags(reviewTags);
        review.setDescription(updateForm.getDescription());
        review.setStarScore(updateForm.getStarScore());
        List<String> imageUrls = awsS3Service.uploadFile(updateForm.getFiles());
        review.setImageUrls(imageUrls);

        reviewTagRepository.saveAll(reviewTags);
        reviewRepository.save(review);
    }

    public List<ReviewDto> findAllByUserId(Long userId) {

        List<Review> reviews = reviewRepository.findAllByUserId(userId);

        return reviews.stream().map(
                review -> ReviewDto.builder()
                        .user(UserDto.Response.fromUser(review.getUser()))
                        .description(review.getDescription())
                        .reviewTags(review.getReviewTags().stream().map(reviewTag -> reviewTag.getTag().getDescription()).toList())
                        .starScore(review.getStarScore())
                        .imageUrl(review.getImageUrls())
                        .timestamp(review.getCreatedDate())
                        .parkId(review.getPark().getId())
                        .reviewId(review.getId())
                        .build()
        ).toList();
    }
}

