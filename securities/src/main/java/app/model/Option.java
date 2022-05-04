package app.model;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
public class Option {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    protected long id;
    @ManyToOne
    Stock stockListing;
    @Column
    OptionType optionType;
    @Column
    BigDecimal strikePrice;
    @Column
    BigDecimal impliedVolatility;
    @Column
    Long openInterest;
    @Column
    String settlementDate;
}
