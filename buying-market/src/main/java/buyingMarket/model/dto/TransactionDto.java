package buyingMarket.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {
    private long receiptId;
    private LocalDateTime time;
    private String security;
    private BigDecimal price;
    private Long volume;

    public TransactionDto(long receiptId, LocalDateTime time, String security, BigDecimal price, Long volume) {
        this.receiptId = receiptId;
        this.time = time;
        this.security = security;
        this.price = price;
        this.volume = volume;
    }
}
