package models;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
// @Table( name = "exchange" )
@Data
public class StockExchange {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;

    @Column( name = "name")
    private String name;

    @Column( name = "MIC" )
    private String mic;

    @Column( name = "country" )
    private String country;

    @Column( name = "time_zone" )
    private String timeZone;            /* UTC se podrazumeva, samo "+h" za sad. */

    @Column( name = "open" )
    private String open;

    @Column( name = "close" )
    private String closed;

    @ManyToMany
    @JoinTable(
            name = "exchange_currencies",
            joinColumns = @JoinColumn( name = "stock_exchange_id" ),
            inverseJoinColumns = @JoinColumn( name = "currency_id" ) )
    Collection<Currency> currencies;

    @OneToMany
    Collection<Stock> stocks;

    public StockExchange() { }

    public StockExchange( String name, String mic, String country, String timeZone, String open, String closed ) {
        this.name = name;
        this.mic = mic;
        this.country = country;
        this.timeZone = timeZone;
        this.open = open;
        this.closed = closed;
    }
}
