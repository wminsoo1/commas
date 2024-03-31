package tight.commas.domain.park.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import tight.commas.domain.park.dto.ParkDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ParkApiTest {

    @Autowired ParkApi parkApi;

    @Test
    void 남은_데이터_조회() {
        // Given
        int page = 131;
        int pageSize = 1; // 한 페이지당 한 개의 데이터만 가져옴

        // When
        List<ParkDto> remainingData = parkApi.getParkRemainingData(page, pageSize);

        // Then
        assertNotNull(remainingData);
        assertFalse(remainingData.isEmpty());

        // 로그를 이용하여 확인
        log.info("마지막 데이터 확인: {}", remainingData);
    }

    @Test
    void 페이징된_자연_관광지_정보_조회() {
        // Given
        int page = 1;
        int pageSize = 100;

        // When
        Page<ParkDto> pagedNaturalTourismInfo = parkApi.getPagedNaturalTourismInfo(page, pageSize);

        // Then
        assertNotNull(pagedNaturalTourismInfo);
        assertTrue(pagedNaturalTourismInfo.hasContent());
        log.info("총 페이지 수: {}", pagedNaturalTourismInfo.getTotalPages());
        log.info("총 항목 수: {}", pagedNaturalTourismInfo.getTotalElements());
        for (ParkDto parkDto : pagedNaturalTourismInfo) {
            log.info("자연 관광지 정보: {}", parkDto);
        }
    }

}