package tight.commas.domain.park.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.exception.ParkErrorCode;
import tight.commas.global.exception.BadRequestException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static tight.commas.domain.park.exception.ParkErrorCode.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ParkRepositoryTest {

    @Autowired
    private ParkRepository parkRepository;

    @Test
    void 파크저장() {
        //given
        Park park = Park.builder()
                .id(1L)
                .parkName("test")
                .build();

        //when
        Park result = parkRepository.save(park);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getParkName()).isEqualTo("test");
    }

    @Test
    void 파크가존재하는지테스트() {
        //given
        Park park = Park.builder()
                .id(1L)
                .parkName("test")
                .build();
        //when
        Park result = parkRepository.save(park);
        Optional<Park> findResult = parkRepository.findById(1L);

        //then
        assertThat(findResult).isNotNull();
        assertThat(findResult.get().getParkName()).isEqualTo("test");
    }

}