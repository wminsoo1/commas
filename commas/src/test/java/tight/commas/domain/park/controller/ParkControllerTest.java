package tight.commas.domain.park.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tight.commas.domain.park.service.ParkService;
import tight.commas.global.exception.ControllerAdvice;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ParkControllerTest {
    @InjectMocks
    private ParkController target;

    @Mock
    private ParkService parkService;


    private MockMvc mockMvc;
    private Gson gson;

    private final Long parkId = 1L;
    private final Long userId = 1L;

    @BeforeEach
    public void init() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new ControllerAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void 파크상세조회실패_파크Id가Null() throws Exception {
        //given
        String url = "/api/park/listpage/park";


        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("userId", "1"));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 파크상세조회실패_유저Id가Null() throws Exception {
        //given
        String url = "/api/park/listpage/park/1";
        //when
        ResultActions resultActions = mockMvc.perform(get(url));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 파크상세조회성공() throws Exception {
        //given
        String url = "/api/park/listpage/park/1";

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
//                .andDo(document("parkDetail",
//                        requestFields(
//                                fieldWithPath("parkId").type(JsonFieldType.NUMBER).description("파크아이디"),
//                                fieldWithPath("userId").type(JsonFieldType.STRING).description("유저아이디")
//                        )));
    }
}