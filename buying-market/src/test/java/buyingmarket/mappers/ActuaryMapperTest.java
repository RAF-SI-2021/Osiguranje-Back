package buyingmarket.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buyingmarket.model.Actuary;
import buyingmarket.model.ActuaryType;
import buyingmarket.model.Agent;
import buyingmarket.model.dto.ActuaryCreateDto;

import java.math.BigDecimal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ActuaryMapper.class})
@ExtendWith(SpringExtension.class)
class ActuaryMapperTest {
    @Autowired
    private ActuaryMapper actuaryMapper;

    /**
     * Method under test: {@link ActuaryMapper#actuaryCreateDtoToActuary(ActuaryCreateDto)}
     */
    @Test
    void testActuaryCreateDtoToActuary() {
        Actuary actualActuaryCreateDtoToActuaryResult = this.actuaryMapper
                .actuaryCreateDtoToActuary(new ActuaryCreateDto(123L));
        assertTrue(actualActuaryCreateDtoToActuaryResult.getActive());
        assertEquals(123L, actualActuaryCreateDtoToActuaryResult.getUserId().longValue());
        assertTrue(actualActuaryCreateDtoToActuaryResult.getOrders().isEmpty());
    }

    /**
     * Method under test: {@link ActuaryMapper#actuaryCreateDtoToActuary(ActuaryCreateDto)}
     */
    @Test
    void testActuaryCreateDtoToActuary2() {
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        Actuary actualActuaryCreateDtoToActuaryResult = this.actuaryMapper
                .actuaryCreateDtoToActuary(new ActuaryCreateDto(123L, valueOfResult, true));
        assertTrue(actualActuaryCreateDtoToActuaryResult.getActive());
        assertEquals(123L, actualActuaryCreateDtoToActuaryResult.getUserId().longValue());
        assertTrue(actualActuaryCreateDtoToActuaryResult.getOrders().isEmpty());
        BigDecimal limit = ((Agent) actualActuaryCreateDtoToActuaryResult).getSpendingLimit();
        assertSame(valueOfResult, limit);
        assertTrue(((Agent) actualActuaryCreateDtoToActuaryResult).getApprovalRequired());
        assertEquals("42", limit.toString());
    }

    /**
     * Method under test: {@link ActuaryMapper#actuaryCreateDtoToActuary(ActuaryCreateDto)}
     */
    @Test
    void testActuaryCreateDtoToActuary3() {
        ActuaryCreateDto actuaryCreateDto = mock(ActuaryCreateDto.class);
        when(actuaryCreateDto.getUserId()).thenReturn(123L);
        when(actuaryCreateDto.getActuaryType()).thenReturn(ActuaryType.SUPERVISOR);
        Actuary actualActuaryCreateDtoToActuaryResult = this.actuaryMapper.actuaryCreateDtoToActuary(actuaryCreateDto);
        assertTrue(actualActuaryCreateDtoToActuaryResult.getActive());
        assertEquals(123L, actualActuaryCreateDtoToActuaryResult.getUserId().longValue());
        assertTrue(actualActuaryCreateDtoToActuaryResult.getOrders().isEmpty());
        verify(actuaryCreateDto).getActuaryType();
        verify(actuaryCreateDto).getUserId();
    }
}

