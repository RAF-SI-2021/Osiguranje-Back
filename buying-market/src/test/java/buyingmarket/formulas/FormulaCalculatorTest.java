package buyingmarket.formulas;

import buyingmarket.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FormulaCalculatorTest {

    @Autowired
    private FormulaCalculator formulaCalculator;

    @Test
    void calculateSecurityFeeTest(){
        Order order = Order.builder()
                .amount(Integer.valueOf(1))
                .limitPrice(BigDecimal.valueOf(10))
                .build();
        BigDecimal price = BigDecimal.valueOf(2);
        BigDecimal fee1 = formulaCalculator.calculateSecurityFee(order, price);
        assertThat(fee1).isEqualTo(BigDecimal.valueOf(.48));

        Order order2 = Order.builder()
                .amount(Integer.valueOf(1))
                .limitPrice(BigDecimal.valueOf(10))
                .build();
        BigDecimal price2 = BigDecimal.valueOf(200);
        BigDecimal fee2 = formulaCalculator.calculateSecurityFee(order2, price2);
        assertThat(fee2).isEqualTo(BigDecimal.valueOf(12));

        Order order3 = Order.builder()
                .amount(Integer.valueOf(1))
                .build();
        BigDecimal price3 = BigDecimal.valueOf(2);
        BigDecimal fee3 = formulaCalculator.calculateSecurityFee(order3, price3);
        assertThat(fee3).isEqualTo(BigDecimal.valueOf(.28));

        Order order4 = Order.builder()
                .amount(Integer.valueOf(1))
                .build();
        BigDecimal price4 = BigDecimal.valueOf(200);
        BigDecimal fee4 = formulaCalculator.calculateSecurityFee(order4, price4);
        assertThat(fee4).isEqualTo(BigDecimal.valueOf(7));
    }
}
