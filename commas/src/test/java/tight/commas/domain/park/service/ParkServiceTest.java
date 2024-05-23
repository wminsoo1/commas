package tight.commas.domain.park.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.dto.ParkReviewDetailDto;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.entity.UserParkLike;
import tight.commas.domain.park.exception.ParkErrorCode;
import tight.commas.domain.park.repository.ParkRepository;
import tight.commas.domain.park.repository.UserParkLikeRepository;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.exception.ReviewErrorCode;
import tight.commas.domain.review.repository.ReviewRepository;
import tight.commas.domain.user.entity.User;
import tight.commas.global.exception.BadRequestException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static tight.commas.domain.park.exception.ParkErrorCode.USERPARK_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class ParkServiceTest {

    @InjectMocks
    private ParkService parkService;

    @Mock
    private ParkRepository parkRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserParkLikeRepository userParkLikeRepository;

    private final Long parkId = 1L;
    private final Long userId = 1L;
    private final Long reviewId = 1L;

    private Park park() {
        return Park.builder()
                .id(parkId)
                .build();
    }

    private User user() {
        return User.builder()
                .id(userId)
                .build();
    }

    private Review review(Park park) {
        return Review.builder()
                .id(reviewId)
                .park(park).build();
    }

    private UserParkLike userParkLike(Park park, User user) {
        return UserParkLike.builder()
                .id(1L)
                .park(park)
                .user(user)
                .build();
    }


    @Test
    void 파크상세조회파크예외테스트() {
        //given
        when(parkRepository.findById(parkId)).thenReturn(Optional.empty());

        //when
        BadRequestException result = assertThrows(BadRequestException.class, () -> parkService.getReviewParkDetailDto(parkId, userId));

        //then
        assertThat(result.getErrorCode()).isEqualTo(ParkErrorCode.PARK_NOT_FOUND);

    }

    @Test
    void 파크상세조회리뷰예외테스트() {
        //given
        when(parkRepository.findById(parkId)).thenReturn(Optional.of(park()));
        when(reviewRepository.findFetchJoinAllByParkId(parkId)).thenReturn(Collections.emptyList());

        //when
        BadRequestException result = assertThrows(BadRequestException.class, () -> parkService.getReviewParkDetailDto(parkId, userId));

        //then
        assertThat(result.getErrorCode()).isEqualTo(ReviewErrorCode.INVALID_REVIEW_DATA);
    }

    @Test
    void 파크상세조회좋아요예외테스트() {
        //given
        Park park = park();
        when(parkRepository.findById(parkId)).thenReturn(Optional.of(park));
        when(reviewRepository.findFetchJoinAllByParkId(parkId)).thenReturn(Arrays.asList(review(park)));
        when(userParkLikeRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        //when
        BadRequestException result = assertThrows(BadRequestException.class, () -> parkService.getReviewParkDetailDto(parkId, userId));

        //then
        assertThat(result.getErrorCode()).isEqualTo(USERPARK_NOT_FOUND);
    }

    @Test
    void 파크상세조회성공() {
        //given
        Park park = park();
        Review review = review(park);
        User user = user();
        UserParkLike userParkLike = userParkLike(park, user);

        when(parkRepository.findById(parkId)).thenReturn(Optional.of(park));
        when(reviewRepository.findFetchJoinAllByParkId(parkId)).thenReturn(Arrays.asList(review));
        when(userParkLikeRepository.findByUserId(userId)).thenReturn(Arrays.asList(userParkLike));

        //when
        ParkReviewDetailDto result = parkService.getReviewParkDetailDto(parkId, userId);

        //then
        assertThat(result.getParkId()).isEqualTo(1L);
        assertThat(result.getReviewCount()).isEqualTo(1L);

        verify(parkRepository, times(1)).findById(parkId);
        verify(reviewRepository, times(1)).findFetchJoinAllByParkId(parkId);
        verify(userParkLikeRepository, times(1)).findByUserId(userId);
    }
}
