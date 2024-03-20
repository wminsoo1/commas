package tight.commas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientUtils {

    private final WebClient webClient;

    public <T> Mono<T> get(String url, Class<T> responseDtoClass) {
        return this.webClient.method(HttpMethod.GET)
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
