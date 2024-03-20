package tight.commas.domain.toilet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tight.commas.domain.toilet.api.ToiletApi;
import tight.commas.domain.toilet.dto.ToiletDto;

import java.util.List;

@RestController
public class ToiletApiController {

    @Autowired
    private ToiletApi toiletApi;

    @GetMapping("/api/toilet")
    public List<ToiletDto> getToilet() {
        return toiletApi.getToiletLocation();
    }
}
