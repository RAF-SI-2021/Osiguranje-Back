package models;

import lombok.Data;
import javax.persistence.*;
import java.util.Collection;

@Entity
// @Table( name = "currency" )
@Data
public class Currency {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;

    @Column( name = "name")
    private String name;

    @Column( name = "iso_code" )
    private String isoCode;

    @Column( name = "symbol" )
    private String symbol;

    @Column( name = "region" )
    private String region;

    @ManyToMany( mappedBy = "currencies" )
    Collection<StockExchange> exchanges;

    // TODO: Dodati polje za procenat inflacije.


    public Currency() { }

    public Currency( String name, String isoCode, String symbol, String region ) {
        this.name = name;
        this.isoCode = isoCode;
        this.symbol = symbol;
        this.region = region;
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Currency currency = (Currency) o;
        return id == currency.id;
    }

}
