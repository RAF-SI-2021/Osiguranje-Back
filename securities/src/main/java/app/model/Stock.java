package app.model;
import lombok.*;
import app.model.Exchange;
import app.model.Security;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends Security {

    @Column
    private Long outstandingShares;
    @Column(precision = 10, scale = 4)
    private BigDecimal dividendYield;

    public Stock(String symbol, String description, Exchange exchange, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume, Long outstandingShares, BigDecimal dividendYield) {
        super(symbol, description, exchange, lastUpdated, price, ask, bid, priceChange, volume, 1);
        this.outstandingShares = outstandingShares;
        this.dividendYield = dividendYield;
    }
}
