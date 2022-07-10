package buyingmarket.formulas;


import buyingmarket.model.ActionType;
import buyingmarket.model.Order;
import buyingmarket.model.OrderState;
import buyingmarket.model.SecurityType;
import buyingmarket.model.dto.SecurityDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {FormulaCalculator.class})
@ExtendWith(SpringExtension.class)
public class FormulaCalculatorTest {



    private Order getOrder(){
        Order order = new Order();
        order.setOrderId(10L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(10L);
        order.setUserId(10L);
        order.setAmount(100);
        order.setAmountFilled(50);
        order.setSecurityType(SecurityType.STOCKS);
        order.setAllOrNone(false);
        order.setMargin(BigDecimal.TEN);
        order.setLimitPrice(BigDecimal.TEN);
        order.setStopPrice(BigDecimal.TEN);
        order.setFee(BigDecimal.ONE);
        order.setActionType(ActionType.BUY);

        return order;
    }

    private SecurityDto getSecurity(){
        SecurityDto securityDto = new SecurityDto();
        securityDto.setPrice(new BigDecimal(100));
        return securityDto;
    }

    @Test
    public void calculateSecurityFeeTest(){

        Order order = getOrder();
        SecurityDto securityDto = getSecurity();

        assertEquals(FormulaCalculator.calculateSecurityFee(order,securityDto).intValue(),12);
    }

    @Test
    public void calculateSecurityFeeNoLimitTest1(){

        Order order = getOrder();
        order.setLimitPrice(null);
        SecurityDto securityDto = getSecurity();

        assertEquals(FormulaCalculator.calculateSecurityFee(order,securityDto).intValue(),7);
    }


    @Test
    public void getEstimatedValueTest(){

        Order order = getOrder();
        order.setLimitPrice(null);
        SecurityDto securityDto = getSecurity();

        assertEquals(FormulaCalculator.getEstimatedValue(order,securityDto),new BigDecimal(1000));
    }

}
