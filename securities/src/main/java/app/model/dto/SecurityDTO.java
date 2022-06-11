package app.model.dto;

import app.model.Security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
public class SecurityDTO {
    protected long id;
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

    public SecurityDTO(Security security) {
        this();
        if (security == null) return;

        this.id = security.getId();
        this.ticker = security.getTicker();
        this.name = security.getName();
        this.exchange = new ExchangeDTO(security.getExchange());
        this.lastUpdated = security.getLastUpdated();
        this.price = security.getPrice();
        this.ask = security.getAsk();
        this.bid = security.getBid();
        this.change = security.getPriceChange();
        this.volume = security.getVolume();
        this.contractSize = security.getContractSize();
        this.changePercent = BigDecimal.valueOf(0L);

        try{
            this.dollarVolume = price.multiply(BigDecimal.valueOf(volume));
            this.nominalValue = price.multiply(BigDecimal.valueOf(contractSize));
            this.changePercent = BigDecimal.valueOf(100L).multiply(change.subtract(price)).divide(price, 4, RoundingMode.HALF_EVEN);
        } catch (Exception e){}

//        if (stock.getSecurityHistory() == null || stock.getSecurityHistory().isEmpty())
//            return;
//
//        this.securityHistory = new ArrayList<>();
//        for (SecurityHistory history: stock.getSecurityHistory()){
//            this.securityHistory.add(new SecurityHistoryDTO(history));
//        }
    }

    public SecurityDTO() {
        this.id = 0L;
        this.ticker = "TICKER";
        this.name = "NAME";
        this.exchange = new ExchangeDTO();
        this.lastUpdated = "LAST_UPDATED";
        this.price = BigDecimal.ONE;
        this.ask = BigDecimal.ONE;
        this.bid = BigDecimal.ONE;
        this.change = BigDecimal.ZERO;
        this.volume = 1L;
        this.contractSize = 1;
        this.changePercent = BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "SecurityDTO{" +
                "ticker='" + ticker + '\'' +
                ", name='" + name + '\'' +
                ", exchange=" + exchange +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", price=" + price +
                ", ask=" + ask +
                ", bid=" + bid +
                ", change=" + change +
                ", volume=" + volume +
                ", contractSize=" + contractSize +
                ", securityHistory=" + securityHistory +
                ", changePercent=" + changePercent +
                ", dollarVolume=" + dollarVolume +
                ", nominalValue=" + nominalValue +
                ", initialMarginCost=" + initialMarginCost +
                ", maintenanceMargin=" + maintenanceMargin +
                '}';
    }
}
