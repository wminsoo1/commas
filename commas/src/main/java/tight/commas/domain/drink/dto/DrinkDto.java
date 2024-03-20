package tight.commas.domain.drink.dto;

import lombok.Data;

@Data
public class DrinkDto {

    private String address;
    private double latitude;
    private double longitude;

    public DrinkDto(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
