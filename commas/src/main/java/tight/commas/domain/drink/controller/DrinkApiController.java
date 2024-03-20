package tight.commas.domain.drink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tight.commas.domain.drink.api.DrinkApi;
import tight.commas.domain.drink.dto.DrinkDto;

import java.util.List;

@RestController
public class DrinkApiController {

    @Autowired
    private DrinkApi drinkApi;

    @GetMapping("/api/drink")
    public List<DrinkDto> getToilet() {
        return drinkApi.getDrinkLocation();
    }
}
