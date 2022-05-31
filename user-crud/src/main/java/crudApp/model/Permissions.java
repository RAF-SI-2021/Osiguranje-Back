package crudApp.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Permissions {
    private boolean admin;
    private boolean stockTrading;
    private boolean stockOverview;
    private boolean contractConclusion;
    private boolean supervisor;
    private boolean agent;

    public Permissions() {}

    public Permissions(boolean admin, boolean stockTrading, boolean stockOverview, boolean contractConclusion, boolean supervisor, boolean agent) {
        this.admin = admin;
        this.stockTrading = stockTrading;
        this.stockOverview = stockOverview;
        this.contractConclusion = contractConclusion;
        this.supervisor = supervisor;
        this.agent = agent;
    }
}
