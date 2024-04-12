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


    @PostMapping("/search/{userId}")
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

    @PostMapping("/recommend")
    public List<ParkReviewDetailDto> recommendPark(@RequestBody LocationRequestDto locationRequestDto) {
        List<ParkReviewDetailDto> parkReviewDetailDtoList = parkService.getReviewParkDetailDtos();

        List<ParkReviewDetailDto> closestParks = parkService.selectClosestParks(locationRequestDto, parkReviewDetailDtoList);

        return closestParks;
    }

    @GetMapping("/like/user")
    public ResponseEntity<List<ParkCardDtoV2>> findAllByUserLike(@RequestParam Long userId) {

        List<ParkCardDtoV2> parks = parkService.findAllByUserLike(userId);

        return ResponseEntity.ok(parks);
    }
}
