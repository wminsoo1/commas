package tight.commas.domain.weather.dto;

import lombok.Getter;

@Getter
public class LocationRequestDto {
    private String latitude;
    private String longitude;

    public LocationRequestDto(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;

    }
}
