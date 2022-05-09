package buyingMarket.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
public class Order {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long orderId;
    @Column
    private long securityId;
    @Column
    private int amount;
    @Column
    private OrderType orderType;
    @Column
    private SecurityType securityType;
    @Column
    private boolean allOrNone;
    @Column
    private boolean margin;

    public Order (long securityId, int amount, OrderType orderType, SecurityType securityType, boolean allOrNone, boolean margin) {
        this.securityId = securityId;
        this.amount = amount;
        this.orderType = orderType;
        this.securityType = securityType;
        this.allOrNone = allOrNone;
        this.margin = margin;
    }

    /*public BigDecimal getEstimatedPrice() {
        return security.getPrice().multiply(BigDecimal.valueOf(amount));
    }*/
}
