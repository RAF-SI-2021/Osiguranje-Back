package buyingmarket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@ToString
@Getter
@Setter
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
}
