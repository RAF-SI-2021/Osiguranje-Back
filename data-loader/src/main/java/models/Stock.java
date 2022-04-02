package models;


import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
// @Table( name = "stock" )
@Data
public class Stock {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;

    @Column
    private String symbol;

    @OneToOne
    private StockExchange stockExchange;

    @Column
    private String description;

    @Column
    private String lastUpdated;

    @Column
    private String price;

    @Column
    private String ask;

    @Column
    private String bid;

    @Column
    private String priceChange;

    @Column
    private String volume;

    @OneToMany
    private Collection<StockHistory> stockHistory;

    public Stock() {  }

    public Stock( String symbol, StockExchange stockExchange, String description, String lastUpdated, String price, String ask, String bid, String priceChange, String volume ) {
        this.symbol = symbol;
        this.stockExchange = stockExchange;
        this.description = description;
        this.lastUpdated = lastUpdated;
        this.price = price;
        this.ask = ask;
        this.bid = bid;
        this.priceChange = priceChange;
        this.volume = volume;
    }
}
