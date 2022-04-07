package com.osiguranje.stocks.model;

import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "stocks")
public class StockModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 5)
    private String symbol;
    @Column(nullable = false)
    private Long outstandingShares;
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

    public StockModel() {
    }

    public StockModel(StockUnit stockUnit, String symbol, Long outstandingShares) {
        this.symbol = symbol;
        this.outstandingShares = outstandingShares;
        this.open = stockUnit.getOpen();
        this.close = stockUnit.getClose();
        this.low = stockUnit.getLow();
        this.high = stockUnit.getHigh();
        this.volume = stockUnit.getVolume();
    }

    public StockModel(Long id, String symbol, Long outstandingShares, Double open, Double close, Double low, Double high, Long volume) {
        this.id = id;
        this.symbol = symbol;
        this.outstandingShares = outstandingShares;
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
