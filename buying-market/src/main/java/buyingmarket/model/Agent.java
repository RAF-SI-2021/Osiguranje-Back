package buyingmarket.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Agent extends Actuary {
    @Column(nullable = false)
    private BigDecimal limit;
    @Column(nullable = false)
    private BigDecimal usedLimit;
    @Column(nullable = false)
    private Boolean approvedOrders;

    public Agent() {
    }

    public Agent(Long userId, BigDecimal limit, Boolean approvedOrders) {
        super(userId);
        this.limit = limit;
        this.approvedOrders = approvedOrders;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getUsedLimit() {
        return usedLimit;
    }

    public void setUsedLimit(BigDecimal usedLimit) {
        this.usedLimit = usedLimit;
    }

    public Boolean getApprovedOrders() {
        return approvedOrders;
    }

    public void setApprovedOrders(Boolean approvedOrders) {
        this.approvedOrders = approvedOrders;
    }
}
