package buyingmarket.model.dto;

import buyingmarket.model.ActionType;
import buyingmarket.model.SecurityType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderCreateDto {
    @NotNull
    private Long securityId;
    @NotNull
    private Integer amount;
    @NotNull
    private SecurityType securityType;
    private Boolean allOrNone;
    private BigDecimal margin;
    private BigDecimal limitPrice;
    private BigDecimal stopPrice;
    private ActionType actionType;

    public OrderCreateDto() {}

    public Long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(Long securityId) {
        this.securityId = securityId;
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

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
