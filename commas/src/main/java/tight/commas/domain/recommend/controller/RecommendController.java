//package tight.commas.domain.recommend.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import tight.commas.domain.park.dto.ParkReviewDetailDto;
//import tight.commas.domain.park.entity.Park;
//import tight.commas.domain.park.service.ParkService;
//import tight.commas.domain.recommend.service.RecommendService;
//import tight.commas.domain.review.entity.Review;
//import tight.commas.domain.weather.dto.LocationRequestDto;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/recommend")
//public class RecommendController {
//
//    private final ParkService parkService;
//    private final RecommendService recommendService;
//
//    @GetMapping("/main")
//    public List<ParkReviewDetailDto> recommendPark(@RequestBody LocationRequestDto locationRequestDto) {
//        // 리뷰 많은 순서대로 20개 불러오기
//        List<ParkReviewDetailDto> parkReviewDetailDtoList = parkService.getReviewParkDetailDtos();
//
//        // 가장 가까운 5개 공원 선택
//        List<ParkReviewDetailDto> closestParks = recommendService.selectClosestParks(locationRequestDto, parkReviewDetailDtoList);
//
//        return closestParks;
//
//    }
//
//
//}
