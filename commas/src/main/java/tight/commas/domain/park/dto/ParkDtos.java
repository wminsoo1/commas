package tight.commas.domain.park.dto;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParkDtos {

    @Valid
    private final List<ParkDto> parkDtos = new ArrayList<>();

}
