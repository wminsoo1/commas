package tight.commas.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.review.dto.ReviewDto;
import tight.commas.domain.review.dto.ReviewPostDto;
import tight.commas.domain.review.dto.ReviewPostWithImageDto;
import tight.commas.domain.review.service.ReviewService;
import tight.commas.domain.user.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/park/{parkId}")
    public ResponseEntity<List<ReviewDto>> findAllByParkId(@PathVariable(value = "parkId") Long parkId) {

        return ResponseEntity.ok(reviewService.findByParkId(parkId));
    }

    @PostMapping("/park/{parkId}")
    public ResponseEntity<Void> postReview(
            @PathVariable(value = "parkId") Long parkId,
            @AuthenticationPrincipal User user,
            @RequestBody ReviewPostDto review
    ) {

        reviewService.postReview(parkId, user, review);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/park/{parkId}/v2")
    public ResponseEntity<Void> postReviewWithImage(
            @PathVariable(value = "parkId") Long parkId,
            @AuthenticationPrincipal User user,
            @ModelAttribute ReviewPostWithImageDto reviewPostWithImageDto
    ) {
        reviewService.postReviewWithImage(parkId, user, reviewPostWithImageDto);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable(value = "reviewId") Long reviewId,
            @AuthenticationPrincipal User user
    ) {
        reviewService.deleteReview(reviewId, user);

        return ResponseEntity.ok(null);
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> modifyReview(
            @PathVariable(value = "reviewId") Long reviewId,
            @AuthenticationPrincipal User user,
            @RequestBody ReviewPostDto reviewModified
    ) {
        reviewService.modifyReview(reviewId, user, reviewModified);

        return ResponseEntity.ok(null);
    }

    @PutMapping("/{reviewId}/v2")
    public ResponseEntity<Void> modifyReviewV2(
            @PathVariable(value = "reviewId") Long reviewId,
            @AuthenticationPrincipal User user,
            @ModelAttribute ReviewPostWithImageDto reviewModified
    ) {
        reviewService.modifyReviewWithImage(reviewId, user, reviewModified);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> findAllByUser(@PathVariable(value = "userId") Long userId) {

        List<ReviewDto> reviews = reviewService.findAllByUserId(userId);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReviewDto>> findMyAllReview(@AuthenticationPrincipal User user) {

        List<ReviewDto> reviews = reviewService.findAllByUserId(user.getId());

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/tagType")
    public ResponseEntity<List<String>> getAllTagType() {

        List<String> tagTypes = reviewService.getAllTagType();

        return ResponseEntity.ok(tagTypes);
    }
}
