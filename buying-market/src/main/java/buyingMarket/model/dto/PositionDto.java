package buyingMarket.model.dto;

import buyingMarket.model.Order;
import buyingMarket.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class PositionDto {
    private Long id;
    private User user;
    private BigDecimal margin;
    private Set<Order> orders;
}
