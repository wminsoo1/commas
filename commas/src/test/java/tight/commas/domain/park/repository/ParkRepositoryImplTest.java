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

}