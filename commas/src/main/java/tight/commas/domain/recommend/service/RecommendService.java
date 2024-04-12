//package tight.commas.domain.recommend.service;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import tight.commas.domain.park.dto.ParkReviewDetailDto;
//import tight.commas.domain.weather.dto.LocationRequestDto;
//
//import java.lang.Math;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class RecommendService {
//
//    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
//        final int R = 6371; // 지구 반지름 (단위: km)
//
//        // 위도 및 경도를 라디안으로 변환
//        double latDistance = Math.toRadians(lat2 - lat1);
//        double lonDistance = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double distance = R * c; // 결과: km
//
//        return distance;
//    }
//
//    public List<ParkReviewDetailDto> selectClosestParks(LocationRequestDto locationRequestDto, List<ParkReviewDetailDto> parkReviewDetailDtoList) {
//        double userLat = Double.parseDouble(locationRequestDto.getLatitude());
//        double userLon = Double.parseDouble(locationRequestDto.getLongitude());
//
//        // 변경 가능한 리스트로 복사
//        List<ParkReviewDetailDto> mutableList = new ArrayList<>(parkReviewDetailDtoList);
//
//        // 공원 리스트를 거리순으로 정렬
//        mutableList.sort(Comparator.comparingDouble(park ->
//                calculateDistance(userLat, userLon, park.getAddress().getLatitude(), park.getAddress().getLongitude())));
//
//        // 가장 가까운 5개 공원 선택
//        int endIndex = Math.min(5, mutableList.size());
//        return new ArrayList<>(mutableList.subList(0, endIndex));
//    }
//
//}
