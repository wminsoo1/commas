package tight.commas.domain.park.dto;

import lombok.Data;

@Data
public class ParkDto {
    private String name;
    private String content;
    private String address;
    private String mainEquip;
    private String mainPlant;
    private String imageUrl;
    private double latitude;
    private double longitude;


    public ParkDto(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public ParkDto(String name, String content, String address, String mainEquip, String mainPlant, String imageUrl, double latitude, double longitude) {
        this.name = name;
        this.content = content;
        this.address = address;
        this.mainEquip = mainEquip;
        this.mainPlant = mainPlant;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

