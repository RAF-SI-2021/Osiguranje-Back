package buyingmarket.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SecurityDto {
    protected int id;
    protected String ticker;
    protected String name;
    protected String lastUpdated;
    protected BigDecimal price;
    protected BigDecimal ask;
    protected BigDecimal bid;
    protected BigDecimal change;
    @NotNull
    protected Long volume;
    protected int contractSize;
    protected Collection<SecurityHistoryDTO> securityHistory;

    protected BigDecimal changePercent;
    protected BigDecimal dollarVolume;
    protected BigDecimal nominalValue;

    protected BigDecimal initialMarginCost;
    protected BigDecimal maintenanceMargin;
}
