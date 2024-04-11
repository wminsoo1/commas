package tight.commas.domain.review.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tight.commas.domain.Address;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.repository.ParkRepository;
import tight.commas.domain.park.service.ParkService;
import tight.commas.domain.review.dto.ReviewPostDto;
import tight.commas.domain.review.repository.ReviewRepository;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkService parkService;

    @BeforeEach
    void BeforeEach() {
//        Page<ParkDto> allParks = parkService.getAllParks(10);
//        parkService.saveAllParksFromDto(allParks);

        Address address = new Address("서울특별시", 10, 20);
        // 기본 Park 데이터 생성
        Park park1 = new Park();
        park1.saveParkInfo("Park1", "공원 설명 내용1", address, "운동기구1", "식물1", "이미지 URL1");
        parkRepository.save(park1);

        Park park2 = new Park();
        park2.saveParkInfo("Park2", "공원 설명 내용2", address, "운동기구2", "식물2", "이미지 URL2");

        parkRepository.save(park2);

        Park park3 = new Park();
        park3.saveParkInfo("Park3", "공원 설명 내용3", address, "운동기구3", "식물3", "이미지 URL3");
        parkRepository.save(park3);

        // 테스트를 위해 Review 객체와 ReviewTag 객체 생성 및 저장
        User user = new User(); // 유저 객체 생성
        userRepository.save(user);
        ReviewPostDto reviewDto1 = new ReviewPostDto();
        reviewDto1.setDescription("Description1"); // 리뷰 설명 설정
        reviewDto1.setStarScore(4); // 리뷰 별점 설정
        List<String> tag1 = Arrays.asList("WALK", "PRETTY", "PICNIC");
        reviewDto1.setReviewTags(tag1); // 리뷰 태그 설정
        ReviewPostDto reviewDto2 = new ReviewPostDto();
        reviewDto2.setDescription("Description2"); // 리뷰 설명 설정
        reviewDto2.setStarScore(5); // 리뷰 별점 설정
        List<String> tag2 = Arrays.asList("PRETTY", "PICNIC");
        reviewDto2.setReviewTags(tag2); // 리뷰 태그 설정

        reviewService.postReviewV2(1L, user, reviewDto1);
        reviewService.postReviewV2(1L, user, reviewDto2);
        em.flush();
        em.clear();
    }

//    void afterEach() {
//        @AfterEach
//        // 모든 공원 데이터 삭제
//        parkRepository.deleteAll();
//        // 모든 리뷰 데이터 삭제
//        reviewRepository.deleteAll();
//        // 모든 사용자 데이터 삭제
//        userRepository.deleteAll();
//    }
}