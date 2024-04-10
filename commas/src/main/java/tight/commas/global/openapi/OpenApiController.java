package tight.commas.global.openapi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.drink.api.DrinkApi;
import tight.commas.domain.drink.dto.DrinkDto;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.service.ParkService;
import tight.commas.domain.toilet.api.ToiletApi;
import tight.commas.domain.toilet.dto.ToiletDto;
import tight.commas.domain.weather.api.WeatherApi;
import tight.commas.domain.weather.dto.WeatherRequestDto;
import tight.commas.domain.weather.dto.WeatherResponseDto;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/openapi")
public class OpenApiController {

    private final ParkService parkService;
    private final WeatherApi weatherApi;
    private final DrinkApi drinkApi;
    private final ToiletApi toiletApi;

    @GetMapping("/park")
    public ResponseEntity<Page<ParkDto>> getPagedParkInfo(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        Page<ParkDto> parkPage = parkService.getAllParks(pageSize);
        parkService.saveAllParksFromDto(parkPage);
        return ResponseEntity.ok(parkPage);
    }

    @GetMapping("/natural-tourism")
    public ResponseEntity<Page<ParkDto>> getPagedNaturalTourismInfo(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        Page<ParkDto> naturalTourismPage = parkService.getAllNaturalTourismInfo(pageSize);
        parkService.saveAllNaturalTourismFromDto(naturalTourismPage);
        return ResponseEntity.ok(naturalTourismPage);
    }

    @GetMapping("/weather")
    public WeatherResponseDto getWeather(@RequestBody WeatherRequestDto weatherRequestDto) throws UnsupportedEncodingException {
        return weatherApi.fetchWeatherData(weatherRequestDto);
    }


    @GetMapping("/drink")
    public List<DrinkDto> getDrink() {
        return drinkApi.getDrinkLocation();
    }

    @GetMapping("/toilet")
    public List<ToiletDto> getToilet() {
        return toiletApi.getToiletLocation();
    }
}
