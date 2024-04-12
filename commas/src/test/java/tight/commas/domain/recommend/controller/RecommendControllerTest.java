package tight.commas.domain.recommend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import tight.commas.domain.park.dto.ParkReviewDetailDto;
import tight.commas.domain.park.service.ParkService;
import tight.commas.domain.recommend.service.RecommendService;
import tight.commas.domain.weather.dto.LocationRequestDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(RecommendController.class)
public class RecommendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkService parkService;

    @MockBean
    private RecommendService recommendService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void recommendParkTest() throws Exception {
        // Given
        LocationRequestDto locationRequestDto = new LocationRequestDto("37.5665", "126.9780");
        List<ParkReviewDetailDto> expectedParks = parkService.getReviewParkDetailDtos();


        given(recommendService.selectClosestParks(locationRequestDto, new ArrayList<>())).willReturn(expectedParks);

        // When & Then
        mockMvc.perform(get("/api/recommend/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedParks)));
    }
}
