package app.model.dto;

import app.model.Stock;

import java.math.BigDecimal;

public class StockDTO extends SecurityDTO {

    private Long outstandingShares;
    private BigDecimal dividendYield;
    private BigDecimal marketCap;

    public StockDTO(Stock stock) {
        super(stock);

        this.outstandingShares = stock.getOutstandingShares();
        this.dividendYield = stock.getDividendYield();
        this.marketCap = price.multiply(BigDecimal.valueOf(outstandingShares));

        this.maintenanceMargin = price.divide(BigDecimal.valueOf(2));
        this.initialMarginCost = maintenanceMargin.multiply(BigDecimal.valueOf(1.1));
    }
}
