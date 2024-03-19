package tight.commas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tight.commas.domain.toilet.ToiletApi;

import java.util.Map;

@RestController
public class ToiletApiController {

    @Autowired
    private ToiletApi toiletApi;

    @GetMapping("/api/toilet")
    public Map<String, Object> getToilet() {
        return toiletApi.getToiletLocation();
    }
}
