package buyingMarket.formulas;

import buyingMarket.enums.OrderType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FormulaCalculator {

    private final BigDecimal marketPercentageFee = new BigDecimal("0.14");
    private final BigDecimal marketFlatFee = new BigDecimal("7");
    private final BigDecimal limitPercentageFee = new BigDecimal("0.24");
    private final BigDecimal limitFlatFee = new BigDecimal("12");

    public BigDecimal calculateSecurityCost(OrderType orderType, Long contractSize, BigDecimal price){

        BigDecimal cost = price.multiply(BigDecimal.valueOf(contractSize));
        switch (orderType){
            case MARKET: {
                BigDecimal percentageFee = cost.multiply(marketPercentageFee);
                if(percentageFee.compareTo(marketFlatFee) < 0)
                    cost = cost.add(percentageFee);
                else cost = cost.add(marketFlatFee);

            }break;
            case LIMIT: {
                BigDecimal percentageFee = cost.multiply(limitPercentageFee);
                if(percentageFee.compareTo(limitFlatFee) < 0)
                    cost = cost.add(percentageFee);
                else cost = cost.add(limitFlatFee);
            }break;
            default:
        }
        return cost;
    }

    public BigDecimal calculateSecurityCost(OrderType orderType, Long contractSize, BigDecimal price, BigDecimal limitPrice){
        if(price.compareTo(limitPrice) < 0)
            return calculateSecurityCost(orderType, contractSize, price);
        else
            return calculateSecurityCost(orderType, contractSize, limitPrice);
    }

}
