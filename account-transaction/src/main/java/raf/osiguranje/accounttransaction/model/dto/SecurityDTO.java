package raf.osiguranje.accounttransaction.model.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
