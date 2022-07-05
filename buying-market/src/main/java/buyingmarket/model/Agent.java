package buyingmarket.model;

import dto.market.ActuaryType;

import javax.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class Agent extends Actuary {

    public Agent() {
    }

    public Agent(Long userId, BigDecimal spendingLimit, Boolean approvalRequired) {
        super(userId, ActuaryType.AGENT);
        this.spendingLimit = spendingLimit;
        this.approvalRequired = approvalRequired;
        this.usedLimit = new BigDecimal(0);
    }
}
