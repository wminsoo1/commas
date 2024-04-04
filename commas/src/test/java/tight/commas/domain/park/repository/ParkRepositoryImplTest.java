package tight.commas.domain.park.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.service.ParkService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ParkRepositoryImplTest {

    @Autowired
    ParkRepositoryImpl parkRepositoryImpl;
    @Autowired
    ParkService parkService;

    @BeforeEach
    void BeforeEach() {
        Page<ParkDto> allParks = parkService.getAllParks(10);
        parkService.saveAllParksFromDto(allParks);
    }

    @Test
    void 페이징_검색() {

        System.out.println("========================================");
        int pageNumber = 0;
        int pageSize = 1;
        boolean hasNextPage = true;
        ParkSearchCondition condition = new ParkSearchCondition();
        condition.setParkName("서울");

        while (hasNextPage) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<ParkDto> searchResultPage = parkRepositoryImpl.search(condition, pageable);
            List<ParkDto> searchResultList = searchResultPage.getContent();


            // 페이지 정보 출력
            System.out.println("페이지 번호: " + searchResultPage.getNumber());
            System.out.println("페이지 크기: " + searchResultPage.getSize());
            System.out.println("전체 항목 수: " + searchResultPage.getTotalElements());
            System.out.println("전체 페이지 수: " + searchResultPage.getTotalPages());
            System.out.println("hasNextPage: " + searchResultPage.hasNext());

            for (ParkDto parkDto : searchResultList) {
                System.out.println("parkDto = " + parkDto);
            }

            if (!searchResultPage.hasNext()) {
                hasNextPage = false;
            } else {
                pageNumber++;
            }
        }
    }
}