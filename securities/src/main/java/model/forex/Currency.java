package model.forex;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Getter
public class Currency {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "iso_code")
    private String isoCode;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "region")
    private String region;


    // TODO: Dodati polje za procenat inflacije.


    public Currency() {
    }

    public Currency(String name, String isoCode, String symbol, String region) {
        this.name = name;
        this.isoCode = isoCode;
        this.symbol = symbol;
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(isoCode, currency.isoCode);
    }


    @Override
    public int hashCode() {
        return 0;
    }


}
