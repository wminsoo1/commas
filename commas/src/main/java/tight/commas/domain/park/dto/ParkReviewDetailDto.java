package tight.commas.domain.park.dto;

import lombok.Data;
import tight.commas.domain.Address;
import tight.commas.domain.park.entity.Park;

import java.util.List;

@Data
public class ParkReviewDetailDto {

    private Long parkId;

    private String parkName;

    private Address address;

    private String imageUrl;

    private int phoneNumber;

    private String plant; //주요 식물

    private String mainEquip; //주요 시설

    private String outLine; //개요

    private Boolean likeStatus;

    private int reviewCount;

//    List<ParkReviewDescriptionDto> parkReviewDescriptionDtos;

    public ParkReviewDetailDto(Park park, int reviewCount) {
        this.parkId = park.getId();
        this.parkName = park.getParkName();
        this.address = park.getAddress();
        this.imageUrl = park.getImageUrl();
        this.phoneNumber = park.getPhoneNumber();
        this.plant = park.getPlant();
        this.mainEquip = park.getMainEquip();
        this.outLine = park.getOutLine();
        this.likeStatus = park.getLikeStatus();
        this.reviewCount = reviewCount;
    }

}
