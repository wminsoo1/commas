package tight.commas.global.openapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tight.commas.domain.drink.api.DrinkApi;
import tight.commas.domain.drink.dto.DrinkDto;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.domain.park.dto.ParkDtos;
import tight.commas.domain.park.service.ParkService;
import tight.commas.domain.toilet.api.ToiletApi;
import tight.commas.domain.toilet.dto.ToiletDto;
import tight.commas.domain.weather.api.WeatherApi;
import tight.commas.domain.weather.dto.LocationRequestDto;
import tight.commas.domain.weather.dto.WeatherResponseDto;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
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
            Pageable pageable) {

        Page<ParkDto> parkPage = parkService.getParks(pageable);
        return ResponseEntity.ok(parkPage);
    }

    @PostMapping("/park")
    public ResponseEntity<HttpStatus> addPagedParkInfo(@RequestBody @Validated ParkDtos parkDtos) {

        parkService.saveAllParksFromDto(parkDtos.getParkDtos());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/natural-tourism")
    public ResponseEntity<Page<ParkDto>> getPagedNaturalTourismInfo(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        Page<ParkDto> naturalTourismPage = parkService.getAllNaturalTourismInfo(pageSize);
        parkService.saveAllNaturalTourismFromDto(naturalTourismPage);
        return ResponseEntity.ok(naturalTourismPage);
    }

    @PostMapping("/weather")
    public WeatherResponseDto getWeather(@RequestBody LocationRequestDto locationRequestDto) throws UnsupportedEncodingException {
        return weatherApi.fetchWeatherData(locationRequestDto);
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
