package buyingMarket.formulas;

import buyingMarket.model.OrderType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FormulaCalculator {

    private final BigDecimal marketPercentageFee = new BigDecimal("0.14");
    private final BigDecimal marketFlatFee = new BigDecimal("7");
    private final BigDecimal limitPercentageFee = new BigDecimal("0.24");
    private final BigDecimal limitFlatFee = new BigDecimal("12");

    public BigDecimal calculateSecurityFee(OrderType orderType, Long contractSize, BigDecimal price){

        BigDecimal fee = price.multiply(BigDecimal.valueOf(contractSize));
        switch (orderType){
            case MARKET: {
                BigDecimal percentageFee = fee.multiply(marketPercentageFee);
                if(percentageFee.compareTo(marketFlatFee) < 0)
                    fee = percentageFee;
                else fee = marketFlatFee;

            }break;
            case LIMIT: {
                BigDecimal percentageFee = fee.multiply(limitPercentageFee);
                if(percentageFee.compareTo(limitFlatFee) < 0)
                    fee = percentageFee;
                else fee = limitFlatFee;
            }break;
            default:
        }
        return fee;
    }

    public BigDecimal calculateSecurityFee(OrderType orderType, Long contractSize, BigDecimal price, BigDecimal limitPrice){
        if(price.compareTo(limitPrice) < 0)
            return calculateSecurityFee(orderType, contractSize, price);
        else
            return calculateSecurityFee(orderType, contractSize, limitPrice);
    }

}
