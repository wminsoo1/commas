package tight.commas.domain.park.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.service.ParkService;

@RestController
public class ParkApiController {

    private final ParkService parkService;

    @Autowired
    public ParkApiController(ParkService parkService) {
        this.parkService = parkService;
    }

    @GetMapping("/api/park")
    public ResponseEntity<Page<ParkDto>> getPagedParkInfo(
            @RequestParam(name =  "pageSize", defaultValue = "10") int pageSize) {

        Page<ParkDto> parkPage = parkService.getAllParks(pageSize);
        return ResponseEntity.ok(parkPage);
    }

    @GetMapping("/api/natural-tourism")
    public ResponseEntity<Page<ParkDto>> getPagedNaturalTourismInfo(
            @RequestParam(name =  "pageSize", defaultValue = "10") int pageSize) {

        Page<ParkDto> naturalTourismPage = parkService.getAllNaturalTourismInfo(pageSize);
        return ResponseEntity.ok(naturalTourismPage);
    }
}
