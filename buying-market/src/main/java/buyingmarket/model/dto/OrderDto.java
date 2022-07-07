package buyingmarket.model.dto;

import buyingmarket.model.ActionType;
import buyingmarket.model.OrderState;
import buyingmarket.model.SecurityType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    @NotNull
    private Long securityId;
    @NotNull
    private Long userId;
    @NotNull
    private Integer amount;
    private Integer amountFilled;
    @NotNull
    private SecurityType securityType;
    private Boolean allOrNone;
    private BigDecimal margin;
    private BigDecimal limitPrice;
    private BigDecimal stopPrice;
    private BigDecimal fee;
    private Set<Long> transactions;
    private OrderState orderState;
    private ActionType actionType;
    private Date modificationDate;

}
