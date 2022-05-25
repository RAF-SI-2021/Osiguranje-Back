package buyingmarket.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Data
@Getter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private String isoCode;

    @Column
    private String symbol;

    @OneToOne
    private Region region;

    @OneToMany(cascade=CascadeType.ALL)
    private Collection<InflationRate> inflationRates;        //TODO

    public Currency() {
    }

    public Currency(String name, String isoCode, String symbol, Region region) {
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
