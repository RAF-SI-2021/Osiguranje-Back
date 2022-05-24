package buyingMarket.model.dto;

import buyingMarket.model.Security;
import buyingMarket.model.SecurityHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SecurityDto {
    protected String ticker;
    protected String name;
    protected ExchangeDTO exchange;
    protected String lastUpdated;
    protected BigDecimal price;
    protected BigDecimal ask;
    protected BigDecimal bid;
    protected BigDecimal change;
    protected Long volume;
    protected int contractSize;
    protected Collection<SecurityHistoryDTO> securityHistory;

    protected BigDecimal changePercent;
    protected BigDecimal dollarVolume;
    protected BigDecimal nominalValue;

    protected BigDecimal initialMarginCost;
    protected BigDecimal maintenanceMargin;

    public SecurityDto(Security stock) {
        this.ticker = stock.getTicker();
        this.name = stock.getName();
        this.exchange = new ExchangeDTO(stock.getExchange());
        this.lastUpdated = stock.getLastUpdated();
        this.price = stock.getPrice();
        this.ask = stock.getAsk();
        this.bid = stock.getBid();
        this.change = stock.getPriceChange();
        this.volume = stock.getVolume();
        this.contractSize = stock.getContractSize();

        if (stock.getSecurityHistory() != null){
            this.securityHistory = new ArrayList<>();
            for (SecurityHistory history: stock.getSecurityHistory()){
                this.securityHistory.add(new SecurityHistoryDTO(history));
            }
        }

        this.changePercent = price.multiply(BigDecimal.valueOf(100)).divide(price.subtract(change));
        this.dollarVolume = price.multiply(BigDecimal.valueOf(volume));
        this.nominalValue = price.multiply(BigDecimal.valueOf(contractSize));
    }
}
