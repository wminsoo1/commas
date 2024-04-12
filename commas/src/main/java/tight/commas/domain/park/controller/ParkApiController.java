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

    @GetMapping("/search/{userId}")
    public ResponseEntity<Page<ParkCardDtoV2>> searchParks(
            @RequestBody ParkSearchCondition condition,
            @PathVariable("userId") Long userId,
            Pageable pageable) {

        Page<ParkCardDtoV2> parkCardDtoV2s = parkService.parkCardDtoV2Page(condition, pageable, userId);
        return ResponseEntity.ok(parkCardDtoV2s);
    }

    @GetMapping("/listpage/park/{parkId}")
    public ResponseEntity<ParkReviewDetailDto> getParkDetail(
            @PathVariable("parkId") Long parkId,
            @RequestParam("userId") Long userId) {

        ParkReviewDetailDto reviewParkDetailDto = parkService.getReviewParkDetailDto(parkId, userId);
        return ResponseEntity.ok(reviewParkDetailDto);
    }

    @GetMapping("/listpage/{userId}")
    public ResponseEntity<Page<ParkCardDtoV2>> getParkListPage(
            Pageable pageable,
            @PathVariable("userId") Long userId) {
        Page<ParkCardDtoV2> parkCard = parkService.getParkCard(pageable, userId);
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
