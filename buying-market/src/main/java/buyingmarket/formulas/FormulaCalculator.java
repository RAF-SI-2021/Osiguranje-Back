package buyingmarket.formulas;

import buyingmarket.model.Order;
import buyingmarket.model.dto.SecurityDto;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class FormulaCalculator {

    private static final BigDecimal marketPercentageFee = new BigDecimal("0.14");
    private static final BigDecimal marketFlatFee = new BigDecimal("7");
    private static final BigDecimal limitPercentageFee = new BigDecimal("0.24");
    private static final BigDecimal limitFlatFee = new BigDecimal("12");

    public static BigDecimal calculateSecurityFee(Order order, SecurityDto security){
        BigDecimal value = getEstimatedValue(order, security);
        if(order.getLimitPrice() == null) {
            BigDecimal percentageFee = value.multiply(marketPercentageFee);
            if(percentageFee.compareTo(marketFlatFee) < 0)
                return percentageFee;
            return marketFlatFee;
        } else {
            BigDecimal percentageFee = value.multiply(limitPercentageFee);
            if(percentageFee.compareTo(limitFlatFee) < 0)
                return percentageFee;
            return limitFlatFee;
        }
    }

    public static long waitTime(Long volume, Integer amount) {
        long waitTime = ThreadLocalRandom.current().nextLong(24 * 60) * 500L;

        if (volume!=null && volume > Math.abs(amount)) {
            long val = Math.min(Math.max(24 * 60 / (volume / Math.abs(amount)),20),50);
            waitTime = ThreadLocalRandom.current().nextLong(val) * 1000L;
        }
        return waitTime + System.currentTimeMillis();
    }

    public static BigDecimal getEstimatedValue(Order order, SecurityDto security) {
        if (order.getLimitPrice() != null) {
            return order.getLimitPrice().multiply(BigDecimal.valueOf(order.getAmount()));
        } else if (order.getStopPrice() != null) {
            return order.getStopPrice().multiply(BigDecimal.valueOf(order.getAmount()));
        } else {
            return security.getPrice().multiply(BigDecimal.valueOf(order.getAmount()));
        }
    }
}
