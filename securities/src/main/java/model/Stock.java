package model;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends Security{

    @Column(nullable = false)
    private Long outstandingShares;

    public Double changePercentage(double priceChange, double price){
        return (100 * priceChange) / (price - priceChange);
    }

    public Double priceVolume(double volume, double price){
        return volume * price;
    }

    public Double marketCap(double price){
        return outstandingShares * price;
    }

    public Stock(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume, Long outstandingShares) {
        super(symbol, description, lastUpdated, price, ask, bid, priceChange, volume);
        this.outstandingShares = outstandingShares;
    }
}
