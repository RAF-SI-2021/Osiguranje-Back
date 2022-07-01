package raf.osiguranje.accounttransaction.model.dto;

import lombok.*;

import java.math.BigDecimal;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

    private Long orderId;
    private Long securityId;
    private Long userId;
    private Integer amount;

    private SecurityType securityType;
    private Boolean allOrNone;
    private BigDecimal margin;
    private BigDecimal limitPrice;
    private BigDecimal stopPrice;
    private BigDecimal fee;
    private BigDecimal cost;
    private Boolean active;

}
