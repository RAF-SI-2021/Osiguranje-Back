package buyingmarket.model.dto;

import buyingmarket.model.SecurityType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
