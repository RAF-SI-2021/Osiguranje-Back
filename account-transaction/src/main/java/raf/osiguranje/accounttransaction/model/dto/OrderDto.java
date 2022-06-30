package raf.osiguranje.accounttransaction.model.dto;

import com.sun.istack.NotNull;
import lombok.*;

import java.math.BigDecimal;

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

}
