package buyingmarket.services;

import buyingmarket.mappers.ActuaryMapper;
import buyingmarket.model.Actuary;
import buyingmarket.model.ActuaryType;
import buyingmarket.model.Agent;
import buyingmarket.model.Supervisor;
import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.repositories.ActuaryRepository;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

    private Actuary iDontCareAnymore(ActuaryCreateDto dto) {
        Actuary actuary;
        if (dto.getActuaryType().equals(ActuaryType.SUPERVISOR)) {
            actuary = new Supervisor(dto.getUserId());
        } else {
            actuary = new Agent(dto.getUserId(), dto.getLimit(), dto.getApprovedOrders());
        }
        return actuary;
    }


    /**
     * Method under test: {@link ActuaryService#getAllAgents()}
     */
    @Test
    void testGetAllAgents() {
        ActuaryCreateDto actuaryCreateDto = new ActuaryCreateDto(1l, new BigDecimal(123), true);

        Mockito.when(actuaryRepository.findAll()).thenReturn(Collections.singletonList(iDontCareAnymore(actuaryCreateDto)));
        actuaryService.createActuary(actuaryCreateDto);

        List<Agent> actualAllAgents = this.actuaryService.getAllAgents();
        assertThat(actualAllAgents.size()).isGreaterThan(0);
    }

    /**
     * Method under test: {@link ActuaryService#createActuary(ActuaryCreateDto)}
     */
    @Test
    void testCreateActuary() {
        Long userId = 2L;

        ActuaryCreateDto dto = new ActuaryCreateDto(userId, new BigDecimal(123), true);
        Mockito.when(actuaryRepository.findActuaryByUserId(userId)).thenReturn(Optional.of(iDontCareAnymore(dto)));
        this.actuaryService.createActuary(dto);

        Optional<Actuary> agent = actuaryRepository.findActuaryByUserId(userId);

        assertThat(agent.isPresent()).isTrue();
        assertThat(agent.get().getUserId()).isEqualTo(userId);
    }

    /**
     * Method under test: {@link ActuaryService#resetLimit(Long)}
     */
    @Test
    void testResetLimit() {
        Long userId = 2L;

        ActuaryCreateDto dto = new ActuaryCreateDto(userId, new BigDecimal(123), true);
        ActuaryCreateDto newDto = new ActuaryCreateDto(userId, BigDecimal.ZERO, true);

        Mockito.when(actuaryRepository.findActuaryByUserId(userId)).thenReturn(Optional.of(iDontCareAnymore(dto)));
        Mockito.when(actuaryRepository.findById(null)).thenReturn(Optional.of(iDontCareAnymore(newDto)));


        Optional<Actuary> agent = actuaryRepository.findActuaryByUserId(userId);
        assertThat(agent.isPresent()).isTrue();

        this.actuaryService.resetLimit(agent.get().getId());
        agent = actuaryRepository.findActuaryByUserId(userId);

        assertThat(agent.isPresent()).isTrue();
        assertThat(new BigDecimal(0)).isEqualTo(agent.get().getUsedLimit());

    }

    /**
     * Method under test: {@link ActuaryService#setLimit(Long, BigDecimal)}
     */
    @Test
    void testSetLimit() {
        Long userId = 2L;

        BigDecimal newLimit = new BigDecimal(123);


        ActuaryCreateDto dto = new ActuaryCreateDto(userId, new BigDecimal(123), true);
        ActuaryCreateDto newDto = new ActuaryCreateDto(userId, newLimit, true);

        Mockito.when(actuaryRepository.findActuaryByUserId(userId)).thenReturn(Optional.of(iDontCareAnymore(dto)));
        Mockito.when(actuaryRepository.findById(null)).thenReturn(Optional.of(iDontCareAnymore(newDto)));

        Optional<Actuary> agent = actuaryRepository.findActuaryByUserId(userId);
        assertThat(agent.isPresent()).isTrue();

        this.actuaryService.setLimit(agent.get().getId(), newLimit);
        agent = actuaryRepository.findActuaryByUserId(userId);
        assertThat(agent.isPresent()).isTrue();

        assertThat(newLimit).isEqualTo(agent.get().getSpendingLimit());
    }

    /**
     * Method under test: {@link ActuaryService#dailyReset()}
     */
//    @Test
//    void testDailyReset() {
//        this.actuaryService.dailyReset();
//
//        Optional<Actuary> agent = actuaryRepository.findActuaryByUserId(1L);
//
//        assertThat(agent.get().getUsedLimit()).isEqualTo(new BigDecimal(1));
//
//    }

//    /**
//     * Method under test: {@link ActuaryService#getActuary(String)}
//     */
//    @Test
//    @Disabled
//    void testGetActuary() {
//        Actuary actuary = this.actuaryService.getActuary("Jws");
//
//        assertThat(actuary).isNotNull();
//    }
}

