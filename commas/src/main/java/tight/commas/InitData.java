package tight.commas;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tight.commas.domain.Address;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.repository.ParkRepository;
import tight.commas.domain.review.dto.ReviewPostDto;
import tight.commas.domain.review.service.ReviewService;
import tight.commas.domain.user.entity.User;
import tight.commas.domain.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    private final ReviewService reviewService;
    private final ParkRepository parkRepository;
    private final UserRepository userRepository;


    @PostConstruct
    public void initData() {
        Address address = new Address("서울특별시", 10, 20);

        // 공원 데이터 생성
        Park park1 = new Park();
        park1.saveParkInfo("Park1", "공원 설명 내용1", address, "운동기구1", "식물1", "이미지 URL1");
        parkRepository.save(park1);

        Park park2 = new Park();
        park2.saveParkInfo("Park2", "공원 설명 내용2", address, "운동기구2", "식물2", "이미지 URL2");
        parkRepository.save(park2);

        Park park3 = new Park();
        park3.saveParkInfo("Park3", "공원 설명 내용3", address, "운동기구3", "식물3", "이미지 URL3");
        parkRepository.save(park3);

        // Review 객체와 ReviewTag 객체 생성 및 저장
        User user = new User();
        userRepository.save(user);

        ReviewPostDto reviewDto1 = new ReviewPostDto();
        reviewDto1.setDescription("Description1");
        reviewDto1.setStarScore(4);
        List<String> tag1 = Arrays.asList("WALK", "PRETTY", "PICNIC");
        reviewDto1.setReviewTags(tag1);

        ReviewPostDto reviewDto2 = new ReviewPostDto();
        reviewDto2.setDescription("Description2");
        reviewDto2.setStarScore(5);
        List<String> tag2 = Arrays.asList("PRETTY", "PICNIC");
        reviewDto2.setReviewTags(tag2);

        reviewService.postReviewV2(1L, user, reviewDto1);
        reviewService.postReviewV2(2L, user, reviewDto2);
    }
}
