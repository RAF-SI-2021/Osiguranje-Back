package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@MappedSuperclass
@Getter
@Setter
public class Security {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    protected long id;
    @Column
    protected String symbol;
    @Column
    protected String description;
    @Column
    protected String lastUpdated;
    @Column
    protected BigDecimal price;
    @Column
    protected BigDecimal ask;
    @Column
    protected BigDecimal bid;
    @Column
    protected BigDecimal priceChange;
    @Column
    protected Long volume;
    @OneToMany
    protected Collection<SecurityHistory> securityHistory;

    public Security() {  }

    public Security(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume) {
        this.symbol = symbol;
        this.description = description;
        this.lastUpdated = lastUpdated;
        this.price = price;
        this.ask = ask;
        this.bid = bid;
        this.priceChange = priceChange;
        this.volume = volume;
    }
}
