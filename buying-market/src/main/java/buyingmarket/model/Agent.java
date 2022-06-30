package buyingmarket.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
public class Agent extends Actuary {
    @Column(nullable = false)
    private BigDecimal spendingLimit;
    @Column(nullable = false)
    private BigDecimal usedLimit;
    @Column(nullable = false)
    private Boolean approvalRequired;
    @Version
    private Integer version;

    public Agent() {
    }

    public Agent(Long userId, BigDecimal spendingLimit, Boolean approvalRequired) {
        super(userId);
        this.spendingLimit = spendingLimit;
        this.approvalRequired = approvalRequired;
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
