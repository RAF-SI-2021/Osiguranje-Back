package buyingmarket.integration;

import buyingmarket.model.Actuary;
import buyingmarket.model.ActuaryType;
import buyingmarket.model.Supervisor;
import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.repositories.ActuaryRepository;
import buyingmarket.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActuaryRepository actuaryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void test1() throws Exception{

        Long userId = 1000L;

        ActuaryCreateDto actuaryDto = new ActuaryCreateDto(userId,new BigDecimal(1223),true);

        mockMvc.perform(post("/api/actuaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actuaryDto)))
                .andExpect(status().isNoContent());

        Optional<Actuary> actuaryOptional = actuaryRepository.findActuaryByUserId(userId);

        assertThat(actuaryOptional.isPresent()).isTrue();
        Actuary realActuary = actuaryOptional.get();
        System.out.println(realActuary.getId());
        assertThat(realActuary.getActive()).isTrue();
        assertThat(realActuary.getUserId().equals(userId)).isTrue();
        assertThat(realActuary.getActuaryType().equals(ActuaryType.AGENT)).isTrue();
    }

    @Test
    public void test2() throws Exception{

        Long userId = 1000L;
        Optional<Actuary> actuaryOptional = actuaryRepository.findActuaryByUserId(userId);

        assertThat(actuaryOptional.isPresent()).isTrue();
        Actuary realActuary = actuaryOptional.get();
        Long id = realActuary.getId();


        mockMvc.perform(put("/api/actuaries/usedLimit/"+id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        actuaryOptional = actuaryRepository.findActuaryByUserId(userId);

        assertThat(actuaryOptional.isPresent()).isTrue();
        realActuary = actuaryOptional.get();

        assertThat(realActuary.getUsedLimit().intValue() == 0).isTrue();
    }

    @Test
    public void test3() throws Exception{

        Long userId = 1000L;

        Optional<Actuary> actuaryOptional = actuaryRepository.findActuaryByUserId(userId);

        assertThat(actuaryOptional.isPresent()).isTrue();
        Actuary realActuary = actuaryOptional.get();
        Long id = realActuary.getId();

        BigDecimal newLimit = new BigDecimal(1000);

        mockMvc.perform(put("/api/actuaries/limit/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLimit))).andExpect(status().isNoContent());

        actuaryOptional = actuaryRepository.findActuaryByUserId(userId);

        assertThat(actuaryOptional.isPresent()).isTrue();
        realActuary = actuaryOptional.get();

        assertThat(realActuary.getSpendingLimit().intValue() == newLimit.intValue()).isTrue();
    }

}
