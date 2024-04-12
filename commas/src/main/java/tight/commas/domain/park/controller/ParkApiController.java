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
import tight.commas.domain.weather.dto.LocationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/api/park")
public class ParkApiController {

    private final ParkService parkService;

    @Autowired
    public ParkApiController(ParkService parkService) {
        this.parkService = parkService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ParkCardDtoV2>> searchParks(
            @RequestBody ParkSearchCondition condition,
            Pageable pageable) {

        Page<ParkCardDtoV2> parkCardDtoV2s = parkService.parkCardDtoV2Page(condition, pageable);
        return ResponseEntity.ok(parkCardDtoV2s);
    }

    @GetMapping("/listpage/{parkId}")
    public ResponseEntity<ParkReviewDetailDto> getParkDetail(
            @PathVariable("parkId") Long parkId) {

        ParkReviewDetailDto reviewParkDetailDto = parkService.getReviewParkDetailDto(parkId);
        return ResponseEntity.ok(reviewParkDetailDto);
    }

    @GetMapping("/listpage")
    public ResponseEntity<Page<ParkCardDtoV2>> getParkListPage(
            Pageable pageable) {
        Page<ParkCardDtoV2> parkCard = parkService.getParkCard(pageable);
        return ResponseEntity.ok(parkCard);
    }

    @GetMapping("/recommend")
    public List<ParkReviewDetailDto> recommendPark(@RequestBody LocationRequestDto locationRequestDto) {
        List<ParkReviewDetailDto> parkReviewDetailDtoList = parkService.getReviewParkDetailDtos();

        List<ParkReviewDetailDto> closestParks = parkService.selectClosestParks(locationRequestDto, parkReviewDetailDtoList);

        return closestParks;

    }
}
