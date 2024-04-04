package tight.commas.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.review.dto.ReviewDto;
import tight.commas.domain.review.dto.ReviewPostDto;
import tight.commas.domain.review.service.ReviewService;
import tight.commas.domain.user.entity.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/park/{parkId}/review")
    public ResponseEntity<List<ReviewDto>> getReview(@PathVariable(value = "parkId") Long parkId) {

        return ResponseEntity.ok(reviewService.getReview(parkId));
    }

    @PostMapping("/park/{parkId}/review")
    public ResponseEntity<Void> postReview(
            @PathVariable(value = "parkId") Long parkId,
            @AuthenticationPrincipal User user,
            @RequestBody ReviewPostDto review
        ) {

        reviewService.postReview(parkId, user, review);

        return ResponseEntity.ok(null);
    }
}
