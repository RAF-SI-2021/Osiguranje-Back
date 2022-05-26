package app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Option extends Security {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    protected long id;
    @ManyToOne
    Stock stockListing;
    @Column
    OptionType optionType;
    @Column(precision = 10, scale = 4)
    BigDecimal strikePrice;
    @Column(precision = 10, scale = 4)
    BigDecimal impliedVolatility;
    @Column
    Long openInterest;
    @Column
    Date settlementDate;

    public Option(String ticker, String name, Exchange exchange, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal change, Long volume, int contractSize) {
        super(ticker, name, exchange, lastUpdated, price, ask, bid, change, volume, contractSize);
    }
}
