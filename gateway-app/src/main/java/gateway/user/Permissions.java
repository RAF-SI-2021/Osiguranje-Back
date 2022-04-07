package gateway.user;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Permissions {
    private boolean isAdmin;
    private boolean stockTrading;
    private boolean stockOverview;
    private boolean contractConclusion;
}