package raf.osiguranje.accounttransaction.model.dto;


import java.math.BigDecimal;
import java.util.Collection;

public class SecurityDTO {
    protected int id;
    protected String ticker;
    protected String name;
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

    public SecurityDTO() {
    }

    public SecurityDTO(int id, String ticker, String name, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal change, Long volume, int contractSize, Collection<SecurityHistoryDTO> securityHistory, BigDecimal changePercent, BigDecimal dollarVolume, BigDecimal nominalValue, BigDecimal initialMarginCost, BigDecimal maintenanceMargin) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.lastUpdated = lastUpdated;
        this.price = price;
        this.ask = ask;
        this.bid = bid;
        this.change = change;
        this.volume = volume;
        this.contractSize = contractSize;
        this.securityHistory = securityHistory;
        this.changePercent = changePercent;
        this.dollarVolume = dollarVolume;
        this.nominalValue = nominalValue;
        this.initialMarginCost = initialMarginCost;
        this.maintenanceMargin = maintenanceMargin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public int getContractSize() {
        return contractSize;
    }

    public void setContractSize(int contractSize) {
        this.contractSize = contractSize;
    }

    public Collection<SecurityHistoryDTO> getSecurityHistory() {
        return securityHistory;
    }

    public void setSecurityHistory(Collection<SecurityHistoryDTO> securityHistory) {
        this.securityHistory = securityHistory;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }

    public BigDecimal getDollarVolume() {
        return dollarVolume;
    }

    public void setDollarVolume(BigDecimal dollarVolume) {
        this.dollarVolume = dollarVolume;
    }

    public BigDecimal getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(BigDecimal nominalValue) {
        this.nominalValue = nominalValue;
    }

    public BigDecimal getInitialMarginCost() {
        return initialMarginCost;
    }

    public void setInitialMarginCost(BigDecimal initialMarginCost) {
        this.initialMarginCost = initialMarginCost;
    }

    public BigDecimal getMaintenanceMargin() {
        return maintenanceMargin;
    }

    public void setMaintenanceMargin(BigDecimal maintenanceMargin) {
        this.maintenanceMargin = maintenanceMargin;
    }
}
