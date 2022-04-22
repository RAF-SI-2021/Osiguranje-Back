package model;

import lombok.Getter;
import lombok.Setter;
import model.Currency;
import model.Security;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Forex extends Security {

    @ManyToOne
    private Currency baseCurrency;
    @ManyToOne
    private Currency quoteCurrency;

    public Forex(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume, Currency baseCurrency, Currency quoteCurrency, Integer contractSize) {
        super(symbol, description, null, lastUpdated, price, ask, bid, priceChange, volume, contractSize);
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
    }

    public Forex(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume, Integer contractSize) {
        super(symbol, description, null, lastUpdated, price, ask, bid, priceChange, volume, contractSize);
    }

    public Forex(){}
}
