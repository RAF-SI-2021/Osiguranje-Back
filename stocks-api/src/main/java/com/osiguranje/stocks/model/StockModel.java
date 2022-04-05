package com.osiguranje.stocks.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
public class StockModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String symbol;
    @Column(nullable = false)
    private Long outstandingShares;

    public StockModel() {
    }

    public StockModel(Long id, String symbol, Long outstandingShares) {
        this.id = id;
        this.symbol = symbol;
        this.outstandingShares = outstandingShares;
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
