package buyingmarket.model.dto;

import buyingmarket.model.SecurityType;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Builder
public class OrderDto {
    private Long orderId;
    @NotNull
    private Long securityId;
    @NotNull
    private Long userId;
    @NotNull
    private Integer amount;
    @NotNull
    private SecurityType securityType;
    private Boolean allOrNone;
    private BigDecimal margin;
    private BigDecimal limitPrice;
    private BigDecimal stopPrice;
    private BigDecimal fee;
    private BigDecimal cost;
    private Boolean active;
    private Set<TransactionDto> transactions;

    public OrderDto() {}

    public OrderDto(Long orderId, Long securityId, Long userId, Integer amount, SecurityType securityType, Boolean allOrNone, BigDecimal margin, BigDecimal limitPrice, BigDecimal stopPrice, BigDecimal fee, BigDecimal cost, Boolean active, Set<TransactionDto> transactions) {
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

    public Set<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDto> transactions) {
        this.transactions = transactions;
    }
}
