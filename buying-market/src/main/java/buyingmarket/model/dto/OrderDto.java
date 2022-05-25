package buyingmarket.model.dto;

import buyingmarket.model.OrderType;
import buyingmarket.model.SecurityType;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private Long securityId;
    private Long userId;
    private Integer amount;
    private OrderType orderType;
    private SecurityType securityType;
    private Boolean allOrNone;
    private BigDecimal margin;
    private BigDecimal price;
    private BigDecimal stopPrice;
    private BigDecimal fee;
    private BigDecimal cost;
    private Boolean active;
    private Set<TransactionDto> transactions;
}
