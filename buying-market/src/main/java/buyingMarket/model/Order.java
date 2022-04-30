package buyingMarket.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    public Order (long securityId, int amount, OrderType orderType, SecurityType securityType, boolean allOrNone, boolean margin, User user, Position position) {
        this.securityId = securityId;
        this.amount = amount;
        this.orderType = orderType;
        this.securityType = securityType;
        this.allOrNone = allOrNone;
        this.margin = margin;
        this.user = user;
        this.position = position;
    }

    /*public BigDecimal getEstimatedPrice() {
        return security.getPrice().multiply(BigDecimal.valueOf(amount));
    }*/
}
