package com.osiguranje.stocks.model;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "stocks")
@NoArgsConstructor
@AllArgsConstructor
public class StockModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "symbol", nullable = false, length = 5)
    private String symbol;
    @Column(nullable = false)
    private Long outstandingShares;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private Double open;
    @Column(nullable = false)
    private Double close;
    @Column(nullable = false)
    private Double low;
    @Column(nullable = false)
    private Double high;
    @Column(nullable = false)
    private Long volume;

    public StockModel(String symbol, Long outstandingShares, Date date, Double open, Double close, Double low, Double high, Long volume) {
        this.symbol = symbol;
        this.outstandingShares = outstandingShares;
        this.date = date;
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
        this.volume = volume;
    }

    public Double changePercentage(double priceChange, double price){
        return (100 * priceChange) / (price - priceChange);
    }

    public Double priceVolume(double volume, double price){
        return volume * price;
    }

    public Double marketCap(double price){
        return outstandingShares * price;
    }
}
