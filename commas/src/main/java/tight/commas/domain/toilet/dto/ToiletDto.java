package tight.commas.domain.toilet.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ToiletDto {

    private String address;
    private double latitude;
    private double longitude;

    public ToiletDto(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
