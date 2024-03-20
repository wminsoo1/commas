package tight.commas.domain.drink.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tight.commas.utils.WebClientUtils;
import tight.commas.domain.drink.dto.DrinkDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DrinkApi {

    @Autowired
    private WebClientUtils webClientUtils;

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088";

    @Value("${api.key}")
    private String apiKey;

    public List<DrinkDto> getDrinkLocation() {
        String key = apiKey;
        String service = "TbViewGisArisu";
        int startIndex = 1;
        int endIndex = 10; // 예시로 5개만 가져오도록 설정

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);

        Map<String, Object> response = responseMono.block();

        Map<String, Object> toiletService = (Map<String, Object>) response.get("TbViewGisArisu");
        List<Map<String, Object>> drinkList = (List<Map<String, Object>>) toiletService.get("row");

        return drinkList.stream()
                .map(toiletData -> {
                    String address = (String) toiletData.get("COT_CONTS_NAME");
                    double latitude = Double.parseDouble((String) toiletData.get("LAT"));
                    double longitude = Double.parseDouble((String) toiletData.get("LNG"));
                    return new DrinkDto(address, latitude, longitude);
                })
                .collect(Collectors.toList());
    }
}
