package com.example.exchangeapp.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockDTO {
    private Long id;
    private String symbol;
    private Long outstandingShares;
    private Double open;
    private Double close;
    private Double low;
    private Double high;
    private Long volume;

    public StockDTO() {
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
