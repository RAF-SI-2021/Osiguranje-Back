package buyingMarket.model.dto;

import buyingMarket.model.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionCreateDto {
    private long userId;
    private LocalDateTime time;
    private String security;
    private BigDecimal price;
    private BigDecimal volume;
    private OrderType orderType;

    public TransactionCreateDto(long userId, LocalDateTime time, String security, BigDecimal price, BigDecimal volume, OrderType orderType) {
        this.userId = userId;
        this.time = time;
        this.security = security;
        this.price = price;
        this.volume = volume;
        this.orderType = orderType;
    }
}
