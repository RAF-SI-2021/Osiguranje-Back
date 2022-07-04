package market;


import lombok.*;
import securities.SecurityType;

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

    private Long securityId;

    private Long userId;

    private Integer amount;
    private Integer amountFilled;

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
