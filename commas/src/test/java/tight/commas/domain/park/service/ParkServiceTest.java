package tight.commas.domain.park.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import tight.commas.domain.park.dto.ParkDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Slf4j
class ParkServiceTest {

    @Autowired
    ParkService parkService;

    @Test
    void 공원_전체_조회() {
        Page<ParkDto> allParks = parkService.getAllParks(10);
        for (ParkDto allPark : allParks) {
            log.info("전체 공원 정보: {}", allPark);
        }
    }

    @Test
    void 모든_자연_관광지_조회() {
        Page<ParkDto> allNaturalTourismInfo = parkService.getAllNaturalTourismInfo(9);
        for (ParkDto parkDto : allNaturalTourismInfo) {
            log.info("자연 관광지 정보: {}", parkDto);
        }
    }

    @Test
    void 모든_공원_DTO_저장() {
        Page<ParkDto> allParks = parkService.getAllParks(10);
        parkService.saveAllParksFromDto(allParks);
        // insert문이 너무 많이 나감, 구글링 해보니 bulk insert를 하면 되는데 jpa에서는 지원 X -> jdbc에서 bulk insert 가능
    }
}
