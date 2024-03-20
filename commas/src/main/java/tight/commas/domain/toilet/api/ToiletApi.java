package tight.commas.domain.toilet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tight.commas.utils.WebClientUtils;
import tight.commas.domain.toilet.dto.ToiletDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ToiletApi {

    @Autowired
    private WebClientUtils webClientUtils;

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088";

    @Value("${api.key}")
    private String apiKey;

    public List<ToiletDto> getToiletLocation() {
        String key = apiKey;
        String service = "SearchPublicToiletPOIService";
        int startIndex = 1;
        int endIndex = 10; // 예시로 5개만 가져오도록 설정

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);

        Map<String, Object> response = responseMono.block();

        Map<String, Object> toiletService = (Map<String, Object>) response.get("SearchPublicToiletPOIService");
        List<Map<String, Object>> toiletList = (List<Map<String, Object>>) toiletService.get("row");

        return toiletList.stream()
                .map(toiletData -> {
                    String address = (String) toiletData.get("FNAME");
                    double latitude = ((Number) toiletData.get("Y_WGS84")).doubleValue();
                    double longitude = ((Number) toiletData.get("X_WGS84")).doubleValue();
                    return new ToiletDto(address, latitude, longitude);
                })
                .collect(Collectors.toList());
    }
}



