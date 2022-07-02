package buyingmarket.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public class TransactionDto {
    private Long id;
    private LocalDateTime time;
    private BigDecimal price;
    private Long volume;
    private Long orderId;

    public TransactionDto() {}

    public TransactionDto(Long id, LocalDateTime time, BigDecimal price, Long volume, Long orderId) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.volume = volume;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
