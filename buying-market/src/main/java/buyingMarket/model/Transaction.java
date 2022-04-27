package buyingMarket.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    protected long id;
    @Column
    protected long userId;
    @Column
    protected String baseCurrencySymbol;
    @Column
    protected String quoteCurrencySymbol;
    @Column
    protected String time;
    @Column
    protected BigDecimal price;
    @Column
    protected BigDecimal volume;

    public Transaction(long userId, String baseCurrencySymbol, String quoteCurrencySymbol, String time, BigDecimal price, BigDecimal volume) {
        this.userId = userId;
        this.baseCurrencySymbol = baseCurrencySymbol;
        this.quoteCurrencySymbol = quoteCurrencySymbol;
        this.time = time;
        this.price = price;
        this.volume = volume;
    }
}
