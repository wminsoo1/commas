package tight.commas.domain.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tight.commas.domain.weather.api.WeatherApi;
import tight.commas.domain.weather.dto.WeatherRequestDto;
import tight.commas.domain.weather.dto.WeatherResponseDto;

import java.io.UnsupportedEncodingException;

@RestController
public class WeatherApiController {

    @Autowired
    private WeatherApi weatherApi;

    @PostMapping("/api/weather")
    public WeatherResponseDto getWeather(@RequestBody WeatherRequestDto weatherRequestDto) throws UnsupportedEncodingException {
        return weatherApi.fetchWeatherData(weatherRequestDto);
    }

}
