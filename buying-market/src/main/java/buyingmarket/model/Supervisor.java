package buyingmarket.model;

import market.ActuaryType;

import javax.persistence.Entity;

@Entity
public class Supervisor extends Actuary {
    public Supervisor() {
    }

    public Supervisor(Long userId) {
        super(userId, ActuaryType.SUPERVISOR);
    }
}
