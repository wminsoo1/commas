package tight.commas.domain.park.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.dto.ParkCardDtoV2;
import tight.commas.domain.park.dto.ParkReviewDetailDto;
import tight.commas.domain.park.service.ParkService;
import tight.commas.domain.weather.dto.LocationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/api/park")
@RequiredArgsConstructor
public class ParkController {

    private final ParkService parkService;

    @PostMapping("/search")
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

    @GetMapping("/main")
    public List<ParkReviewDetailDto> recommendPark(@RequestBody LocationRequestDto locationRequestDto) {
        // 리뷰 많은 순서대로 20개 불러오기
        List<ParkReviewDetailDto> parkReviewDetailDtoList = parkService.getReviewParkDetailDtos();

        // 가장 가까운 5개 공원 선택
        List<ParkReviewDetailDto> closestParks = parkService.selectClosestParks(locationRequestDto, parkReviewDetailDtoList);

        return closestParks;

    }
}
