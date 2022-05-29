package buyingmarket.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityHistoryDTO {

    private String date;
    private BigDecimal price;
    private BigDecimal ask;
    private BigDecimal bid;
    private BigDecimal change;
    private Long volume;
}
