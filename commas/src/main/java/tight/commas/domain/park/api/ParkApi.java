package tight.commas.domain.park.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tight.commas.domain.park.dto.ParkDto;
import tight.commas.utils.WebClientUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ParkApi {

    private final WebClientUtils webClientUtils;

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088";

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    public ParkApi(WebClientUtils webClientUtils) {
        this.webClientUtils = webClientUtils;
    }

    public List<ParkDto> getParkInfo() {
        String key = apiKey;
        String service = "SearchParkInfoService";
        int startIndex = 1;
        int endIndex = 10;

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);

        Map<String, Object> response = responseMono.block();

        Map<String, Object> parkService = (Map<String, Object>) response.get(service);
        List<Map<String, Object>> parkList = (List<Map<String, Object>>) parkService.get("row");

        return parkList.stream()
                .map(parkData -> {
                    String name = (String) parkData.get("P_PARK");
                    String content = (String) parkData.get("P_LIST_CONTENT");
                    String address = (String) parkData.get("P_ADDR");
                    String mainEquip = (String) parkData.get("MAIN_EQUIP");
                    String mainPlant = (String) parkData.get("MAIN_PLANTS");
                    String imageUrl = (String) parkData.get("P_IMG");
                    double latitude = Double.parseDouble((String) parkData.get("LATITUDE"));
                    double longitude = Double.parseDouble((String) parkData.get("LONGITUDE"));

                    return new ParkDto(name, content, address, mainEquip, mainPlant, imageUrl, latitude, longitude);
                }).collect(Collectors.toList());
    }

    public List<ParkDto> getNaturalTourismInfo() {
        String key = apiKey;
        String service = "TbVwNature";
        int startIndex = 1;
        int endIndex = 100;

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);

        Map<String, Object> response = responseMono.block();

        Map<String, Object> naturalTourismService = (Map<String, Object>) response.get(service);
        List<Map<String, Object>> naturalTourismList = (List<Map<String, Object>>) naturalTourismService.get("row");

        return naturalTourismList.stream()
                .filter(naturalTourismData -> "ko".equals(naturalTourismData.get("LANG_CODE_ID")))
                .map(naturalTourismData -> {
                    String name = (String) naturalTourismData.get("POST_SJ");
                    String address = (String) naturalTourismData.get("NEW_ADDRESS");

                    return new ParkDto(name, address);
                }).collect(Collectors.toList());
    }
}

