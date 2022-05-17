package buyingMarket.model.dto;

import buyingMarket.model.Order;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private LocalDateTime time;
    private BigDecimal price;
    private Long volume;
    private Long orderId;
}
