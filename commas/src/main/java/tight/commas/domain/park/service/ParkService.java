package tight.commas.domain.park.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkService {

    private final ParkApi parkApi;
    private final ParkRepository parkRepository;
    private final EntityManager em;
    private final ReviewRepository reviewRepository;

    public Page<ParkDto> getAllParks(int pageSize) {
        List<ParkDto> allParkDtoList = new ArrayList<>();
        int currentPageNum = 1;
        int totalPages = 0;

        while (true) {
            Page<ParkDto> parkPage = parkApi.getPagedParkInfo(currentPageNum, pageSize);
            List<ParkDto> parkDtoList = parkPage.getContent();
            allParkDtoList.addAll(parkDtoList);
            totalPages = parkPage.getTotalPages();
            if (parkPage.isLast()) {
                int remainingItems = parkApi.getTotalCount("SearchParkInfoService") % pageSize;
                List<ParkDto> remainingData = parkApi.getParkRemainingData(currentPageNum * pageSize, currentPageNum * pageSize + remainingItems);
                allParkDtoList.addAll(remainingData);
                break; // 반복문 종료
            }
            currentPageNum++;
        }

        // 모든 데이터를 합쳐서 새로운 Page 객체 생성하여 반환
        return new PageImpl<>(allParkDtoList, PageRequest.of(0, allParkDtoList.size()), totalPages);
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

        return new ParkReviewDetailDto(findPark, parkReviewDescriptionDtos);
    }

    //파크카드 조회
    public Page<ParkCardDtoV2> getParkCard(Pageable pageable) {
        return parkRepository.getParkCardDtoV2(pageable);
    }
}
