package dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permissions {
    private boolean isAdmin;
    private boolean stockTrading;
    private boolean stockOverview;
    private boolean contractConclusion;

    public Permissions() {}

    public Permissions(boolean admin, boolean stockTrading, boolean stockOverview, boolean contractConclusion) {
        this.isAdmin = admin;
        this.stockTrading = stockTrading;
        this.stockOverview = stockOverview;
        this.contractConclusion = contractConclusion;
    }
}
