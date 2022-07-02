package buyingmarket.model;

import buyingmarket.mappers.SetToLongConverter;
import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "orders")
@Builder
@AllArgsConstructor
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
    private Integer amountFilled;
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
    private OrderState orderState;
    @Column
    private ActionType actionType;
    @Column
    private Date modificationDate;
    @ManyToOne
    private Supervisor approvingActuary;
    @ManyToOne
    private Actuary actuary;

    @Column
    @Convert(converter = SetToLongConverter.class)
    private Set<Long> transactions;

    public Order() {}

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

    public Integer getAmountFilled() {
        return amountFilled;
    }

    public void setAmountFilled(Integer amountFilled) {
        this.amountFilled = amountFilled;
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

    public Actuary getActuary() {
        return actuary;
    }

    public void setActuary(Actuary actuary) {
        this.actuary = actuary;
    }

    public Set<Long> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Long> transactions) {
        this.transactions = transactions;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Actuary getApprovingActuary() {
        return approvingActuary;
    }

    public void setApprovingActuary(Supervisor approvingActuary) {
        this.approvingActuary = approvingActuary;
    }
}
