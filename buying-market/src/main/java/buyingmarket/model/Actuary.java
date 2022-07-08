package buyingmarket.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public class Actuary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private Long userId;
    @Column(nullable = false)
    private Boolean active;
    @OneToMany
    private Collection<Order> orders;

    @Column
    protected BigDecimal spendingLimit;
    @Column
    protected BigDecimal usedLimit;
    @Column
    protected Boolean approvalRequired;

    @Column
    private ActuaryType actuaryType;

    @Version
    private Integer version;

    public Actuary() {
        this.orders = new HashSet<>();
        this.spendingLimit = BigDecimal.ZERO;
        this.usedLimit = BigDecimal.ZERO;
        this.approvalRequired = false;
    }

    public Actuary(Long userId,ActuaryType actuaryType) {
        this.userId = userId;
        this.active = true;
        this.actuaryType = actuaryType;
        this.orders = new HashSet<>();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
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

    public ActuaryType getActuaryType() {
        return actuaryType;
    }

    public void setActuaryType(ActuaryType actuaryType) {
        this.actuaryType = actuaryType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
