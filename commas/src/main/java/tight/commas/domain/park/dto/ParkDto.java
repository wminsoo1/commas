package tight.commas.domain.park.dto;

import jakarta.persistence.GeneratedValue;
import lombok.Data;
import tight.commas.domain.Address;

@Data
public class ParkDto {
    private String name;
    private String content;
    private String mainEquip;
    private String mainPlant;
    private String imageUrl;
    private double latitude;
    private double longitude;


    public ParkDto(String lang, String name, Address address) {
        this.lang = lang;
        this.name = name;
        this.address = address;
    }

    public ParkDto(String name, String content, Address address, String mainEquip, String mainPlant, String imageUrl) {
        this.name = name;
        this.content = content;
        this.address = address;
        this.mainEquip = mainEquip;
        this.mainPlant = mainPlant;
        this.imageUrl = imageUrl;
    }
}

