package buyingmarket.services;

import buyingmarket.mappers.ActuaryMapper;
import buyingmarket.model.Actuary;
import buyingmarket.model.Agent;
import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.repositories.ActuaryRepository;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {ActuaryService.class})
@ExtendWith(SpringExtension.class)
class ActuaryServiceTest {
    @MockBean
    private ActuaryMapper actuaryMapper;

    @MockBean
    private ActuaryRepository actuaryRepository;

    @Autowired
    private ActuaryService actuaryService;

    @BeforeEach
    public void init() {
        Agent agent = new Agent(1l, new BigDecimal(12), true);
        actuaryRepository.save(agent);
    }

    /**
     * Method under test: {@link ActuaryService#getAllAgents()}
     */
    @Test
    void testGetAllAgents() {
        List<Agent> actualAllAgents = this.actuaryService.getAllAgents();

        assertThat(actualAllAgents).isNotEqualTo(new ArrayList<Agent>());
    }

    /**
     * Method under test: {@link ActuaryService#createActuary(ActuaryCreateDto)}
     */
    @Test
    void testCreateActuary() {
        ActuaryCreateDto dto = new ActuaryCreateDto(1l);

        this.actuaryService.createActuary(dto);

    }

    /**
     * Method under test: {@link ActuaryService#resetLimit(Long)}
     */
    @Test
    void testResetLimit() {
        Long agentId = 1l;

        this.actuaryService.resetLimit(agentId);

        List<Agent> actualAllAgents = this.actuaryService.getAllAgents();

        for(Agent a : actualAllAgents){
            if(agentId == a.getId()){
                assertThat(new BigDecimal(0)).isEqualTo(a.getUsedLimit());
            }
        }

    }

    /**
     * Method under test: {@link ActuaryService#setLimit(Long, BigDecimal)}
     */
    @Test
    void testSetLimit() {
        Long agentId = 1l;
        ActuaryCreateDto dto = new ActuaryCreateDto(agentId);

        this.actuaryService.createActuary(dto);

        BigDecimal newLimit = new BigDecimal(123);

        this.actuaryService.setLimit(agentId, newLimit);
        List<Agent> actualAllAgents = this.actuaryService.getAllAgents();
        for(Agent a : actualAllAgents){
            if(agentId == a.getId()){
                assertThat(newLimit).isEqualTo(a.getSpendingLimit());
            }
        }
    }

    /**
     * Method under test: {@link ActuaryService#dailyReset()}
     */
    @Test
    void testDailyReset() {
        List<Agent> actualAllAgents = this.actuaryService.getAllAgents();
        this.actuaryService.dailyReset();

        for(Agent a : actualAllAgents){
            assertThat(a.getUsedLimit()).isEqualTo(new BigDecimal(0));
        }

    }

    /**
     * Method under test: {@link ActuaryService#getActuary(String)}
     */
    @Test
    @Disabled
    void testGetActuary() {
        Actuary actuary = this.actuaryService.getActuary("Jws");

        assertThat(actuary).isNotNull();
    }
}

