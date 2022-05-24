package buyingMarket.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@NoArgsConstructor
public class InflationRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Currency currency;

    @Column
    private Date date;

    @Column
    private Float value;

    public InflationRate(Currency currency, Date date, Float value) {
        this.currency = currency;
        this.date = date;
        this.value = value;
    }
}
