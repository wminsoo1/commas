package tight.commas.domain.park.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tight.commas.domain.Address;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkCardDto;
import tight.commas.domain.park.dto.ParkCardDtoV2;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.service.ParkService;
import tight.commas.domain.review.Tag;
import tight.commas.domain.review.dto.ReviewPostDto;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.entity.ReviewTag;
import tight.commas.domain.review.repository.ReviewRepository;
import tight.commas.domain.review.service.ReviewService;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.yaml.snakeyaml.tokens.Token.ID.Tag;
import static tight.commas.domain.review.Tag.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ParkRepositoryImplTest {

    @Autowired
    ParkRepositoryImpl parkRepositoryImpl;
    @Autowired
    ParkService parkService;
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
        reviewDto2.setStarScore(3); // 리뷰 별점 설정
        List<String> tag2 = Arrays.asList("WALK", "PRETTY", "DOG_FRIENDLY");
        reviewDto2.setReviewTags(tag2); // 리뷰 태그 설정
        ReviewPostDto reviewDto3 = new ReviewPostDto();
        reviewDto3.setDescription("Description3"); // 리뷰 설명 설정
        reviewDto3.setStarScore(2); // 리뷰 별점 설정
        List<String> tag3 = Arrays.asList("WALK", "PRETTY", "BIKE_TRAIL", "DOG_FRIENDLY");
        reviewDto3.setReviewTags(tag3); // 리뷰 태그 설정
        ReviewPostDto reviewDto4 = new ReviewPostDto();
        reviewDto4.setDescription("Description4"); // 리뷰 설명 설정
        reviewDto4.setStarScore(1); // 리뷰 별점 설정
        List<String> tag4 = Arrays.asList("WALK", "PRETTY", "DOG_FRIENDLY");
        reviewDto4.setReviewTags(tag4); // 리뷰 태그 설정

        reviewService.postReviewV2(1L, user, reviewDto1);
        reviewService.postReviewV2(1L, user, reviewDto2);
        reviewService.postReviewV2(1L, user, reviewDto3);
        reviewService.postReviewV2(1L, user, reviewDto4);
        em.flush();
        em.clear();
    }

    @AfterEach
    void afterEach() {
        // 모든 공원 데이터 삭제
        parkRepository.deleteAll();
        // 모든 리뷰 데이터 삭제
        reviewRepository.deleteAll();
        // 모든 사용자 데이터 삭제
        userRepository.deleteAll();
    }

    @Test
    void 페이징_검색() {

        System.out.println("========================================");
//        int pageNumber = 0;
//        int pageSize = 1;
//        boolean hasNextPage = true;
//        ParkSearchCondition condition = new ParkSearchCondition();
//        condition.setParkName("서울");
//
//        while (hasNextPage) {
//            Pageable pageable = PageRequest.of(pageNumber, pageSize);
//            Page<ParkDto> searchResultPage = parkRepositoryImpl.search(condition, pageable);
//            List<ParkDto> searchResultList = searchResultPage.getContent();
//
//
//            // 페이지 정보 출력
//            System.out.println("페이지 번호: " + searchResultPage.getNumber());
//            System.out.println("페이지 크기: " + searchResultPage.getSize());
//            System.out.println("전체 항목 수: " + searchResultPage.getTotalElements());
//            System.out.println("전체 페이지 수: " + searchResultPage.getTotalPages());
//            System.out.println("hasNextPage: " + searchResultPage.hasNext());
//
//            for (ParkDto parkDto : searchResultList) {
//                System.out.println("parkDto = " + parkDto);
//            }
//
//            if (!searchResultPage.hasNext()) {
//                hasNextPage = false;
//            } else {
//                pageNumber++;
//            }
//        }
    }

    @Test
    void example() {
        ParkSearchCondition condition = new ParkSearchCondition();
        condition.setParkName("Park");
        List<Tag> tags = Arrays.asList(PRETTY, PICNIC);
        condition.setTags(tags);
        List<Long> example = parkRepository.example(condition);
        for (Long aLong : example) {
            System.out.println("aLong = " + aLong);
        }

    }

    @Test
    void parkCardSearch() {
        // 테스트를 위한 조건 설정
        ParkSearchCondition condition = new ParkSearchCondition();
//        List<Tag> tags = Arrays.asList(PRETTY);

        condition.setTags(Arrays.asList(WALK, PRETTY));
        // 페이징 정보 설정 (첫 페이지, 페이지 사이즈 2)
        PageRequest pageable = PageRequest.of(0, 3);

        System.out.println("========================================");
        Page<ParkCardDto> parkCardDtos = parkRepository.parkCardSearch(condition, pageable); //쿼리 2번
        System.out.println("========================================");
        for (ParkCardDto parkCardDto : parkCardDtos) {
            System.out.println("parkCardDto = " + parkCardDto);
        }
    }

    @Test
    void parkCardSearchV2() {
        em.clear();
        // 테스트를 위한 조건 설정
        ParkSearchCondition condition = new ParkSearchCondition();
        condition.setParkName("Park1");
        condition.setTags(Arrays.asList(PICNIC, PRETTY));

        // 페이징 정보 설정 (첫 페이지, 페이지 사이즈 2)
        PageRequest pageable = PageRequest.of(0, 3);

        System.out.println("========================================");
        Page<ParkCardDtoV2> parkCardDtos = parkRepository.parkCardSearchV2(condition, pageable); // 1 + batchSize
        System.out.println("========================================");
        for (ParkCardDtoV2 parkCardDto : parkCardDtos) {
            System.out.println("parkCardDto = " + parkCardDto);
        }
    }

    @Test
    void parkCardSearchV3() {
        em.clear();
        // 테스트를 위한 조건 설정
        ParkSearchCondition condition = new ParkSearchCondition();
        condition.setParkName("Park");

        // 페이징 정보 설정 (첫 페이지, 페이지 사이즈 2)
        PageRequest pageable = PageRequest.of(0, 3);

        ParkSearchCondition condition1 = new ParkSearchCondition();
        condition1.setParkName("Park");
        System.out.println("========================================");
        Page<ParkCardDtoV2> parkCardDtos = parkRepository.parkCardSearchV3(condition1, pageable);
        System.out.println("========================================");
        for (ParkCardDtoV2 parkCardDto : parkCardDtos) {
            System.out.println("parkCardDto = " + parkCardDto);
        }
    }

    @Test
    void getParkCardDtoV2() {
        Page<ParkCardDtoV2> parkCard = parkRepository.getParkCardDtoV2(PageRequest.of(0, 10));
        for (ParkCardDtoV2 parkCardDtoV2 : parkCard) {
            System.out.println("parkCardDtoV2 = " + parkCardDtoV2);
        }
    }

    @Test
    void parkCardSearchV4() {
        ParkSearchCondition condition = new ParkSearchCondition();
        condition.setParkName("");
        List<Tag> tags = Arrays.asList(PICNIC);
        condition.setTags(tags);
        System.out.println("==================================");
        Page<ParkCardDtoV2> parkCardDtoV2s = parkRepository.parkCardSearchV4(condition, PageRequest.of(0, 10));
        System.out.println("==================================");
        for (ParkCardDtoV2 parkCardDtoV2 : parkCardDtoV2s) {
            System.out.println("parkCardDtoV2 = " + parkCardDtoV2);
        }
    }
}