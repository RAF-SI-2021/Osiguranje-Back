package buyingmarket.formulas;

import buyingmarket.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FormulaCalculator {

    private final BigDecimal marketPercentageFee = new BigDecimal("0.14");
    private final BigDecimal marketFlatFee = new BigDecimal("7");
    private final BigDecimal limitPercentageFee = new BigDecimal("0.24");
    private final BigDecimal limitFlatFee = new BigDecimal("12");

    public BigDecimal calculateSecurityFee(Order order, BigDecimal price){

        BigDecimal fee = price.multiply(BigDecimal.valueOf(order.getAmount()));
        if(order.getLimitPrice() == null) {
            BigDecimal percentageFee = fee.multiply(marketPercentageFee);
            if(percentageFee.compareTo(marketFlatFee) < 0)
                fee = percentageFee;
            else fee = marketFlatFee;
        } else {
            BigDecimal percentageFee = fee.multiply(limitPercentageFee);
            if(percentageFee.compareTo(limitFlatFee) < 0)
                fee = percentageFee;
            else fee = limitFlatFee;
        }
        return fee;
    }

    public BigDecimal calculateSecurityFee(Order order, BigDecimal price, BigDecimal limitPrice){
        if(price.compareTo(limitPrice) < 0)
            return calculateSecurityFee(order, price);
        else
            return calculateSecurityFee(order, limitPrice);
    }

}
