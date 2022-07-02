package buyingmarket.model.dto;

import buyingmarket.model.ActuaryType;

import java.math.BigDecimal;

public class ActuaryCreateDto {
    private Long userId;
    private ActuaryType actuaryType;
    private BigDecimal limit;
    private Boolean approvedOrders;

    public ActuaryCreateDto() {}

    public ActuaryCreateDto(Long userId) {
        this.userId = userId;
        this.actuaryType = ActuaryType.SUPERVISOR;
    }

    public ActuaryCreateDto(Long userId, BigDecimal limit, Boolean approvedOrders) {
        this.userId = userId;
        this.actuaryType = ActuaryType.AGENT;
        this.limit = limit;
        this.approvedOrders = approvedOrders;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ActuaryType getActuaryType() {
        return actuaryType;
    }

    public void setActuaryType(ActuaryType actuaryType) {
        this.actuaryType = actuaryType;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public Boolean getApprovedOrders() {
        return approvedOrders;
    }

    public void setApprovedOrders(Boolean approvedOrders) {
        this.approvedOrders = approvedOrders;
    }
}
