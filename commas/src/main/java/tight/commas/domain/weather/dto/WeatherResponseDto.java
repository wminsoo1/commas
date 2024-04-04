package tight.commas.domain.weather.dto;

import lombok.Getter;

@Getter
public class WeatherResponseDto {
    private int humidity;    //습도
    private String weather;     //날씨
    private int temp;        //온도
    private int pm10_value;
    private int pm25_value;

    public WeatherResponseDto(){

    }

    public void putApiData(String weather, int humidity, int temp, int pm10_value, int pm25_value){
        this.weather = weather;
        this.humidity = humidity;
        this.temp = temp;
        this.pm10_value = pm10_value;
        this.pm25_value = pm25_value;
    }

}