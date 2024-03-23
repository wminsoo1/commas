package tight.commas.domain.weather.dto;

import lombok.Getter;

@Getter
public class WeatherRequestDto {
    private String latitude;
    private String longitude;

    public WeatherRequestDto(){

    }
}
