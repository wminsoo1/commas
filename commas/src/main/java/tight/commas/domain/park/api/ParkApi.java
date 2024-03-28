package tight.commas.domain.park.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tight.commas.domain.Address;
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

    public int getTotalCount(String service) {
        String key = apiKey;
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/1/1/")
                .buildAndExpand(key, service)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);
        Map<String, Object> response = responseMono.block();

        Map<String, Object> serviceData = (Map<String, Object>) response.get(service);
        return (int) serviceData.get("list_total_count");
    }

    public List<ParkDto> getParkRemainingData(int startIndex, int endIndex) {

        String key = apiKey;
        String service = "SearchParkInfoService";

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
                    int id = Integer.parseInt((String) parkData.get("P_IDX"));
                    String name = (String) parkData.get("P_PARK");
                    String content = (String) parkData.get("P_LIST_CONTENT");
                    String addressStr = (String) parkData.get("P_ADDR");
                    String mainEquip = (String) parkData.get("MAIN_EQUIP");
                    String mainPlant = (String) parkData.get("MAIN_PLANTS");
                    String imageUrl = (String) parkData.get("P_IMG");
                    double latitude = ((String) parkData.get("LATITUDE")).isEmpty() ? 0.0 : Double.parseDouble((String) parkData.get("LATITUDE"));
                    double longitude = ((String) parkData.get("LONGITUDE")).isEmpty() ? 0.0 : Double.parseDouble((String) parkData.get("LONGITUDE"));
                    Address address = new Address(addressStr, latitude, longitude);
                    return new ParkDto(id, name, content, address, mainEquip, mainPlant, imageUrl);
                }).collect(Collectors.toList());
    }

    public List<ParkDto> getNaturalTourismRemainingData(int startIndex, int endIndex) {

        String key = apiKey;
        String service = "TbVwNature";

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);

        Map<String, Object> response = responseMono.block();

        Map<String, Object> naturalTourismService = (Map<String, Object>) response.get(service);
        List<Map<String, Object>> naturalTourismList = (List<Map<String, Object>>) naturalTourismService.get("row");

        return naturalTourismList.stream()
                .map(naturalTourismData -> {
                    String lang = (String) naturalTourismData.get("LANG_CODE_ID");
                    String name = (String) naturalTourismData.get("POST_SJ");
                    String addressStr = (String) naturalTourismData.get("NEW_ADDRESS");
                    Address address = new Address(addressStr);
                    return new ParkDto(lang, name, address);
                }).collect(Collectors.toList());
    }

    private Double parseDoubleOrNull(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return Double.parseDouble(str);
    }

    public Page<ParkDto> getPagedParkInfo(int pageNum, int pageSize) {
        String key = apiKey;
        String service = "SearchParkInfoService";
        int startIndex = (pageNum - 1) * pageSize + 1;
        int endIndex = pageNum * pageSize;

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);

        Map<String, Object> response = responseMono.block();

        Map<String, Object> parkService = (Map<String, Object>) response.get(service);
        List<Map<String, Object>> parkList = (List<Map<String, Object>>) parkService.get("row");
        int totalCount = (int) parkService.get("list_total_count");


        List<ParkDto> parkDtoList = parkList.stream()
                .map(parkData -> {
                    int id = Integer.parseInt((String) parkData.get("P_IDX"));
                    String name = (String) parkData.get("P_PARK");
                    String content = (String) parkData.get("P_LIST_CONTENT");
                    String addressStr = (String) parkData.get("P_ADDR");
                    String mainEquip = (String) parkData.get("MAIN_EQUIP");
                    String mainPlant = (String) parkData.get("MAIN_PLANTS");
                    String imageUrl = (String) parkData.get("P_IMG");
                    double latitude = parkData.get("LATITUDE") != null ? Double.parseDouble((String) parkData.get("LATITUDE")) : null;
                    double longitude = parkData.get("LONGITUDE") != null ? Double.parseDouble((String) parkData.get("LONGITUDE")) : null;
                    Address address = new Address(addressStr, latitude, longitude);
                    return new ParkDto(id, name, content, address, mainEquip, mainPlant, imageUrl);
                }).collect(Collectors.toList());

        return new PageImpl<>(parkDtoList, PageRequest.of(pageNum, pageSize), totalCount);
    }

    public Page<ParkDto> getPagedNaturalTourismInfo(int pageNum, int pageSize) {
        String key = apiKey;
        String service = "TbVwNature";
        int startIndex = (pageNum - 1) * pageSize + 1;
        int endIndex = pageNum * pageSize;

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{key}/json/{service}/{startIndex}/{endIndex}/")
                .buildAndExpand(key, service, startIndex, endIndex)
                .toUriString();

        Mono<Map> responseMono = webClientUtils.get(url, Map.class);

        Map<String, Object> response = responseMono.block();

        Map<String, Object> naturalTourismService = (Map<String, Object>) response.get(service);
        List<Map<String, Object>> naturalTourismList = (List<Map<String, Object>>) naturalTourismService.get("row");
        int totalCount = (int) naturalTourismService.get("list_total_count");

        List<ParkDto> parkDtoList = naturalTourismList.stream()
                .map(naturalTourismData -> {
                    String lang = (String) naturalTourismData.get("LANG_CODE_ID");
                    String name = (String) naturalTourismData.get("POST_SJ");
                    String addressStr = (String) naturalTourismData.get("NEW_ADDRESS");
                    Address address = new Address(addressStr);
                    return new ParkDto(lang, name, address);
                }).collect(Collectors.toList());

        return new PageImpl<>(parkDtoList, PageRequest.of(pageNum, pageSize), totalCount);
    }
}
