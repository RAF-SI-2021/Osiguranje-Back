package buyingmarket.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long orderId;
    @Column
    private Long securityId;
    @Column
    private Long userId;
    @Column
    private Integer amount;
    @Column
    private SecurityType securityType;
    @Column
    private Boolean allOrNone;
    @Column
    private BigDecimal margin;
    @Column
    private BigDecimal limitPrice;
    @Column
    private BigDecimal stopPrice;
    @Column
    private BigDecimal fee;
    @Column
    private BigDecimal cost;
    @Column
    private Boolean active;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;
}
