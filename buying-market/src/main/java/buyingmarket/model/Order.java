package buyingmarket.model;

import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "orders")
@Builder
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
    @ManyToOne
    private Actuary actuary;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Transaction> transactions;

    public Order() {}

    public Order(Long orderId, Long securityId, Long userId, Integer amount, SecurityType securityType, Boolean allOrNone, BigDecimal margin, BigDecimal limitPrice, BigDecimal stopPrice, BigDecimal fee, BigDecimal cost, Boolean active, Actuary actuary, Set<Transaction> transactions) {
        this.orderId = orderId;
        this.securityId = securityId;
        this.userId = userId;
        this.amount = amount;
        this.securityType = securityType;
        this.allOrNone = allOrNone;
        this.margin = margin;
        this.limitPrice = limitPrice;
        this.stopPrice = stopPrice;
        this.fee = fee;
        this.cost = cost;
        this.active = active;
        this.actuary = actuary;
        this.transactions = transactions;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(Long securityId) {
        this.securityId = securityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public Boolean getAllOrNone() {
        return allOrNone;
    }

    public void setAllOrNone(Boolean allOrNone) {
        this.allOrNone = allOrNone;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Actuary getActuary() {
        return actuary;
    }

    public void setActuary(Actuary actuary) {
        this.actuary = actuary;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}
