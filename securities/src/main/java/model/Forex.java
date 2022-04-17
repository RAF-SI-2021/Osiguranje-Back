package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Forex extends Security{

    @Column(nullable = false)
    private String baseCurrency;
    @Column(nullable = false)
    private String quoteCurrency;
    @Column(nullable = false)
    private Integer contractSize;

    public BigDecimal nominalValue(BigDecimal lotSize){
        return lotSize.multiply(price);
    }

    public Forex(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume, String baseCurrency, String quoteCurrency, Integer contractSize) {
        super(symbol, description, null, lastUpdated, price, ask, bid, priceChange, volume);
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.contractSize = contractSize;
    }

    public Forex(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume) {
        super(symbol, description, null, lastUpdated, price, ask, bid, priceChange, volume);
    }

    public Forex(){}
}
