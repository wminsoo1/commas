package tight.commas.domain.park.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tight.commas.domain.chat.dto.ChatRoomDto;
import tight.commas.domain.chat.entity.ChatRoom;
import tight.commas.domain.chat.repository.ChatRoomRepository;
import tight.commas.domain.park.ParkSearchCondition;
import tight.commas.domain.park.api.ParkApi;
import tight.commas.domain.park.dto.ParkCardDtoV2;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.dto.ParkReviewDescriptionDto;
import tight.commas.domain.park.dto.ParkReviewDetailDto;
import tight.commas.domain.park.entity.Park;
import tight.commas.domain.park.repository.ParkRepository;
import tight.commas.domain.review.entity.Review;
import tight.commas.domain.review.repository.ReviewRepository;
import tight.commas.domain.weather.dto.LocationRequestDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkService {

    private final ParkApi parkApi;
    private final ParkRepository parkRepository;
    private final EntityManager em;
    private final ReviewRepository reviewRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Page<ParkDto> getParks(Pageable pageable) {

        return parkApi.getPagedParkInfo(pageable.getPageNumber(), pageable.getPageSize());
    }

    public Page<ParkDto> getAllNaturalTourismInfo(int pageSize) {
        List<ParkDto> allNaturalTourismDtoList = new ArrayList<>();
        int currentPageNum = 1;
        int totalPages = 0;

        while (true) {
            Page<ParkDto> naturalTourismPage = parkApi.getPagedNaturalTourismInfo(currentPageNum, pageSize);
            List<ParkDto> naturalTourismDtoList = naturalTourismPage.getContent();

            for (ParkDto dto : naturalTourismDtoList) {
                if ("ko".equals(dto.getLang())) {
                    allNaturalTourismDtoList.add(dto);
                }
            }

            totalPages = naturalTourismPage.getTotalPages();

            if (naturalTourismPage.isLast()) {
                int remainingItems = parkApi.getTotalCount("TbVwNature") % pageSize;
                if (remainingItems > 0) {
                    List<ParkDto> remainingData = parkApi.getNaturalTourismRemainingData(currentPageNum * pageSize, currentPageNum * pageSize + remainingItems);
                    for (ParkDto dto : remainingData) {
                        if ("ko".equals(dto.getLang())) {
                            allNaturalTourismDtoList.add(dto);
                        }
                    }
                }
                break; // 반복문 종료
            }
            currentPageNum++;
        }

        // 모든 데이터를 합쳐서 새로운 Page 객체 생성하여 반환
        return new PageImpl<>(allNaturalTourismDtoList, PageRequest.of(0, allNaturalTourismDtoList.size()), totalPages);
    }

    @Transactional
    public void saveAllParksFromDto(Page<ParkDto> parkDtoPage) {
        List<ParkDto> parkDtoList = parkDtoPage.getContent();
        for (ParkDto parkDto : parkDtoList) {
            // 이름으로 Park 엔티티 찾기
            Park existingPark = parkRepository.findByParkName(parkDto.getName());
            ChatRoom chatRoom;
            if (existingPark != null) {
                // 해당 이름을 가진 Park이 이미 존재하는 경우, 변경 감지를 통해 업데이트
                existingPark.saveParkInfo(
                        parkDto.getName(),
                        parkDto.getContent(),
                        parkDto.getAddress(),
                        parkDto.getMainEquip(),
                        parkDto.getMainPlant(),
                        parkDto.getImageUrl()
                );
                // 이미 JPA에 의해 관리되는 기존 엔티티이므로 저장/업데이트 메서드 호출 불필요
            } else {
                // 해당 이름을 가진 Park이 존재하지 않는 경우, 새로운 엔티티 생성 및 저장
                Park park = new Park();
                park.saveParkInfo(
                        parkDto.getName(),
                        parkDto.getContent(),
                        parkDto.getAddress(),
                        parkDto.getMainEquip(),
                        parkDto.getMainPlant(),
                        parkDto.getImageUrl()
                );
                parkRepository.save(park); // 새로운 Park 엔티티 저장
                chatRoom = ChatRoom.createChatRoom(park);
                chatRoomRepository.save(chatRoom);
            }
        }
    }

    public void saveAllNaturalTourismFromDto(Page<ParkDto> parkDtoPage) {
        List<ParkDto> parkDtoList = parkDtoPage.getContent();
        List<Park> parksToSave = parkDtoList.stream()
                .map(parkDto -> {
                    Park park = new Park();
                    park.saveNaturalTourismInfo(
                            parkDto.getName(),
                            parkDto.getAddress()
                    );
                    return park;
                })
                .collect(Collectors.toList());

        parkRepository.saveAll(parksToSave);
    }

    public Page<ParkCardDtoV2> parkCardDtoV2Page(ParkSearchCondition condition, Pageable pageable) {
        return parkRepository.parkCardSearchV4(condition, pageable);
    }

    //파크 상세 조회
    public ParkReviewDetailDto getReviewParkDetailDto(Long parkId) {
        Park findPark = parkRepository.findById(parkId).orElse(null);
        List<Review> fetchJoinAllByParkId = reviewRepository.findFetchJoinAllByParkId(parkId);

        List<ParkReviewDescriptionDto> parkReviewDescriptionDtos = fetchJoinAllByParkId.stream()
                .map(ParkReviewDescriptionDto::new)
                .toList();

        return new ParkReviewDetailDto(findPark, fetchJoinAllByParkId.size());
    }

    //파크카드 조회
    public Page<ParkCardDtoV2> getParkCard(Pageable pageable) {
        return parkRepository.getParkCardDtoV2(pageable);
    }

    public List<ParkReviewDetailDto> getReviewParkDetailDtos() {

        List<Review> allReviewsWithParksJoinFetch = reviewRepository.findAllReviewsWithParksJoinFetch();

        Map<Park, List<Review>> reviewsByPark  = allReviewsWithParksJoinFetch.stream()
                .collect(Collectors.groupingBy(Review::getPark));

        return reviewsByPark.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .limit(20)
                .map(entry -> new ParkReviewDetailDto(entry.getKey(), entry.getValue().size()))
                .toList();
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (단위: km)

        // 위도 및 경도를 라디안으로 변환
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // 결과: km

        return distance;
    }

    public List<ParkReviewDetailDto> selectClosestParks(LocationRequestDto locationRequestDto, List<ParkReviewDetailDto> parkReviewDetailDtoList) {
        double userLat = Double.parseDouble(locationRequestDto.getLatitude());
        double userLon = Double.parseDouble(locationRequestDto.getLongitude());

        // 변경 가능한 리스트로 복사
        List<ParkReviewDetailDto> mutableList = new ArrayList<>(parkReviewDetailDtoList);

        // 공원 리스트를 거리순으로 정렬
        mutableList.sort(Comparator.comparingDouble(park ->
                calculateDistance(userLat, userLon, park.getAddress().getLatitude(), park.getAddress().getLongitude())));

        // 가장 가까운 5개 공원 선택
        int endIndex = Math.min(5, mutableList.size());
        return new ArrayList<>(mutableList.subList(0, endIndex));
    }
}
