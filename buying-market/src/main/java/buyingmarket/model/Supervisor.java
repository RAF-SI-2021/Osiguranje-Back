package buyingmarket.model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Supervisor extends Actuary {
    public Supervisor() {
    }

    public Supervisor(Long userId) {
        super(userId,ActuaryType.SUPERVISOR);
        this.spendingLimit = BigDecimal.ZERO;
        this.usedLimit = BigDecimal.ZERO;
        this.approvalRequired = false;
    }
}
