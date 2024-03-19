package tight.commas.domain.toilet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@Slf4j
public class ToiletApi {

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088";

    @Value("${api.key}")
    private String apiKey;


    public Map<String, Object> getToiletLocation() {
        String key = apiKey;
        String service = "SearchPublicToiletPOIService";
        int startIndex = 1;
        int endIndex = 100; // 예시로 5개만 가져오도록 설정

        // webClient 기본 설정
        WebClient webClient =
                WebClient
                        .builder()
                        .baseUrl(BASE_URL)
                        .build();

        // api 요청
        return webClient
                        .get()
                        .uri(uriBuilder ->
                                uriBuilder
                                        .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                                        .build(key, service, startIndex, endIndex))
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
    }
}

