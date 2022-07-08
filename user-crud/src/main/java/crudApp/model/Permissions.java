package crudApp.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Permissions {
    private boolean isAdmin;
    private boolean stockTrading;
    private boolean stockOverview;
    private boolean contractConclusion;
    private boolean supervisor;
    private boolean traineeAgent;
    private boolean agent;

    public Permissions() {}

    public Permissions(boolean isAdmin, boolean stockTrading, boolean stockOverview, boolean contractConclusion, boolean supervisor, boolean traineeAgent, boolean agent) {
        this.isAdmin = isAdmin;
        this.stockTrading = stockTrading;
        this.stockOverview = stockOverview;
        this.contractConclusion = contractConclusion;
        this.supervisor = supervisor;
        this.traineeAgent = traineeAgent;
        this.agent = agent;
    }
}
