package buyingmarket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
public class SecurityHistory {

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id
    private long id;
    @Column
    private String date;
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal ask;
    @Column
    private BigDecimal bid;
    @Column
    private BigDecimal priceChange;
    @Column
    private Long volume;

    public SecurityHistory(String date, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume) {
        this.date = date;
        this.price = price;
        this.ask = ask;
        this.bid = bid;
        this.priceChange = priceChange;
        this.volume = volume;
    }
}
