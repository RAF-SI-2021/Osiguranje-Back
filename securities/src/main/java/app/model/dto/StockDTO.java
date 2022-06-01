package app.model.dto;

import app.model.Stock;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
public class StockDTO extends SecurityDTO {

    private Long outstandingShares;
    private BigDecimal dividendYield;
    private BigDecimal marketCap;

    public StockDTO(Stock stock) {
        super(stock);

        this.outstandingShares = 1L;
        this.dividendYield = BigDecimal.ONE;
        try {
            this.marketCap = BigDecimal.ONE;
            this.maintenanceMargin = BigDecimal.ONE;
            this.initialMarginCost = BigDecimal.ONE;
        } catch (Exception e){}

        if (stock == null) return;

        this.outstandingShares = stock.getOutstandingShares();
        this.dividendYield = stock.getDividendYield();
        try {
            this.marketCap = price.multiply(BigDecimal.valueOf(outstandingShares));
            this.maintenanceMargin = price.divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_EVEN);
            this.initialMarginCost = maintenanceMargin.multiply(BigDecimal.valueOf(1.1));
        } catch (Exception e){}
    }

    @Override
    public String toString() {
        return "StockDTO{" +
                "outstandingShares=" + outstandingShares +
                ", dividendYield=" + dividendYield +
                ", marketCap=" + marketCap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockDTO stockDTO = (StockDTO) o;
        return Objects.equals(outstandingShares, stockDTO.outstandingShares) && Objects.equals(dividendYield, stockDTO.dividendYield) && Objects.equals(marketCap, stockDTO.marketCap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outstandingShares, dividendYield, marketCap);
    }
}




