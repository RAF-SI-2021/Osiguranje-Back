package buyingMarket.model.dto;

import buyingMarket.model.OrderType;
import buyingMarket.model.SecurityType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private long orderId;
    private long securityId;
    private int amount;
    private OrderType orderType;
    private SecurityType securityType;
    private boolean allOrNone;
    private boolean margin;

    public OrderDto(long orderId, long securityId, int amount, OrderType orderType, SecurityType securityType, boolean allOrNone, boolean margin) {
        this.orderId = orderId;
        this.securityId = securityId;
        this.amount = amount;
        this.orderType = orderType;
        this.securityType = securityType;
        this.allOrNone = allOrNone;
        this.margin = margin;
    }
}
