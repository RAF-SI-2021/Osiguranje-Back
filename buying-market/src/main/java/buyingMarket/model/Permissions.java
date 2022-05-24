package buyingMarket.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Permissions {
    private boolean isAdmin;
    private boolean stockTrading;
    private boolean stockOverview;
    private boolean contractConclusion;

    public Permissions() {}

    public Permissions(boolean isAdmin, boolean stockTrading, boolean stockOverview, boolean contractConclusion) {
        this.isAdmin = isAdmin;
        this.stockTrading = stockTrading;
        this.stockOverview = stockOverview;
        this.contractConclusion = contractConclusion;
    }
}
