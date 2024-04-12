package tight.commas.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientUtils {

    private final WebClient webClient;

    public static WebClient createWebClient(String baseUrl) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public <T> Mono<T> get(String url, Class<T> responseDtoClass) {
        WebClient client = createWebClient(url);
        return client.method(HttpMethod.GET)
                .uri(url)
                .retrieve()
                .bodyToMono(responseDtoClass);
    }

    public <T, V> Mono<T> post(String url, V requestDto, Class<T> responseDtoClass) {
        return this.webClient.method(HttpMethod.POST)
                .uri(url)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(responseDtoClass);
    }
}
