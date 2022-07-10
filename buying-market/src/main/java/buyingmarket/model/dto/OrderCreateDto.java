package buyingmarket.model.dto;

import buyingmarket.model.ActionType;
import buyingmarket.model.SecurityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {
    @NotNull
    private Long securityId;
    @NotNull
    private Integer amount;
    @NotNull
    private SecurityType securityType;
    private Boolean allOrNone;
    private BigDecimal margin;
    private BigDecimal limitPrice;
    private BigDecimal stopPrice;
    private ActionType actionType;


}
