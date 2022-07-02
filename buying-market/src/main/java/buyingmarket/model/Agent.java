package buyingmarket.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class Agent extends Actuary {
    @Column
    private BigDecimal spendingLimit;
    @Column
    private BigDecimal usedLimit;
    @Column
    private Boolean approvalRequired;

    public Agent() {
    }

    public Agent(Long userId, BigDecimal spendingLimit, Boolean approvalRequired) {
        super(userId);
        this.spendingLimit = spendingLimit;
        this.approvalRequired = approvalRequired;
        this.usedLimit = new BigDecimal(0);
    }

    public BigDecimal getSpendingLimit() {
        return spendingLimit;
    }

    public void setSpendingLimit(BigDecimal spendingLimit) {
        this.spendingLimit = spendingLimit;
    }

    public BigDecimal getUsedLimit() {
        return usedLimit;
    }

    public void setUsedLimit(BigDecimal usedLimit) {
        this.usedLimit = usedLimit;
    }

    public Boolean getApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(Boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }
}
