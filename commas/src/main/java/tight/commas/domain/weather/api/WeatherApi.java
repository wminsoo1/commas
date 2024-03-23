package tight.commas.domain.weather.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tight.commas.domain.weather.dto.WeatherRequestDto;
import tight.commas.domain.weather.dto.WeatherResponseDto;
import tight.commas.utils.WebClientUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WeatherApi {
    @Value("${pm.service.key}")
    private String serviceKey;
    @Value("${open.weather.api.key}")
    private String apiKey;
    private final String pmBaseUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
    private final String sidoName = "서울";
    private final String pageNo = "1";
    private final String numOfRows = "100";
    private final String returnType = "json";
    private final String version = "1.1";
    private final String weatherBaseUrl = "http://api.openweathermap.org/data/2.5/weather";
    private final String language = "kr";

    @Autowired
    private WebClientUtils webClientUtils;

    public WeatherResponseDto fetchWeatherData(WeatherRequestDto weatherRequestDto) throws UnsupportedEncodingException {
        String weatherUrl = String.format("%s?lat=%s&lon=%s&appid=%s&lang=%s", weatherBaseUrl, weatherRequestDto.getLatitude(), weatherRequestDto.getLongitude(), apiKey, language);
        String encodedSidoName = URLEncoder.encode(sidoName, "UTF-8");
        String pmUrl = String.format("%s?sidoName=%s&pageNo=%s&numOfRows=%s&returnType=%s&serviceKey=%s&ver=%s",pmBaseUrl, encodedSidoName, pageNo, numOfRows, returnType, serviceKey, version);

        Mono<Map> weatherResponseMono = webClientUtils.post(weatherUrl, weatherRequestDto, Map.class);
        Map<String, Object> weatherResponse = weatherResponseMono.block();
        Mono<Map> pmResponseMono = webClientUtils.get(pmUrl, Map.class);
        Map<String, Object> pmResponse = pmResponseMono.block();

        // 결과 처리
        List<Map<String, Object>> weathers = (List<Map<String, Object>>) weatherResponse.get("weather");
        String weather = weathers != null && !weathers.isEmpty() ? (String) weathers.get(0).get("description") : "Unknown";
        Map<String, Object> main = (Map<String, Object>) weatherResponse.get("main");
        double absoluteTemp = main != null ? ((Number) main.get("temp")).doubleValue() - 273.15 : 0;
        int temp = (int) Math.round(absoluteTemp);
        int humidity = main != null ? ((Number) main.get("humidity")).intValue() : 0;

        Map<String, Object> responseBody = (Map<String, Object>) pmResponse.get("response");
        Map<String, Object> body = responseBody != null ? (Map<String, Object>) responseBody.get("body") : null;
        List<Map<String, Object>> items = body != null ? (List<Map<String, Object>>) body.get("items") : null;
        int pm10Value = items != null && !items.isEmpty() ? Integer.parseInt((String) items.get(1).get("pm10Value")) : 0;
        int pm25Value = items != null && !items.isEmpty() ? Integer.parseInt((String) items.get(1).get("pm25Value")) : 0;

        WeatherResponseDto weatherResponseDto = new WeatherResponseDto();
        weatherResponseDto.putApiData(weather, humidity, temp, pm10Value, pm25Value);

        return weatherResponseDto;
    }
}
