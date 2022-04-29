package app.model;

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
    protected String ticker;
    @Column
    protected String name;
    @OneToOne
    protected Exchange exchange;
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
    @Column
    protected int contractSize;
    @OneToMany(cascade=CascadeType.ALL)
    protected Collection<SecurityHistory> securityHistory;

    public Security() {  }

    public Security(String ticker, String name, Exchange exchange, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal change, Long volume, int contractSize) {
        this.ticker = ticker;
        this.name = name;
        this.exchange = exchange;
        this.lastUpdated = lastUpdated;
        this.price = price;
        this.ask = ask;
        this.bid = bid;
        this.priceChange = change;
        this.volume = volume;
        this.contractSize = contractSize;
    }
}
