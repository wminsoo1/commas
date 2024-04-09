package tight.commas.domain.park.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkCardDtoV2;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.dto.ParkReviewDetailDto;
import tight.commas.domain.park.service.ParkService;
import tight.commas.domain.review.Tag;

import java.util.List;

@RestController
public class ParkApiController {

    private final ParkService parkService;

    @Autowired
    public ParkApiController(ParkService parkService) {
        this.parkService = parkService;
    }

    @GetMapping("/api/park")
    public ResponseEntity<Page<ParkDto>> getPagedParkInfo(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        Page<ParkDto> parkPage = parkService.getAllParks(pageSize);
        parkService.saveAllParksFromDto(parkPage);
        return ResponseEntity.ok(parkPage);
    }

    @GetMapping("/api/natural-tourism")
    public ResponseEntity<Page<ParkDto>> getPagedNaturalTourismInfo(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        Page<ParkDto> naturalTourismPage = parkService.getAllNaturalTourismInfo(pageSize);
        parkService.saveAllNaturalTourismFromDto(naturalTourismPage);
        return ResponseEntity.ok(naturalTourismPage);
    }

    @GetMapping("/api/search")
    public ResponseEntity<Page<ParkCardDtoV2>> searchParks(
            @RequestBody ParkSearchCondition condition,
            Pageable pageable) {

        Page<ParkCardDtoV2> parkCardDtoV2s = parkService.parkCardDtoV2Page(condition, pageable);
        return ResponseEntity.ok(parkCardDtoV2s);
    }

    @GetMapping("/api/listpage/{id}")
    public ResponseEntity<ParkReviewDetailDto> getParkDetail(
            @PathVariable("id") Long parkId) {

        ParkReviewDetailDto reviewParkDetailDto = parkService.getReviewParkDetailDto(parkId);
        return ResponseEntity.ok(reviewParkDetailDto);
    }
}
