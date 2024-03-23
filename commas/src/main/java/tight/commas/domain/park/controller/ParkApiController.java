package tight.commas.domain.park.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tight.commas.domain.park.api.ParkApi;
import tight.commas.domain.park.dto.ParkDto;

import java.util.List;

@RestController
public class ParkApiController {

    private final ParkApi parkApi;

    @Autowired
    public ParkApiController(ParkApi parkApi) {
        this.parkApi = parkApi;
    }

    @GetMapping("/api/park")
    public ResponseEntity<List<ParkDto>> getParkInfo() {

        return ResponseEntity.ok(parkApi.getParkInfo());
    }

    @GetMapping("/api/naturalTourism")
    public ResponseEntity<List<ParkDto>> getNaturalTourismInfo() {

        return ResponseEntity.ok(parkApi.getNaturalTourismInfo());
    }
}



