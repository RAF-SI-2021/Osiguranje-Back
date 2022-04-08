package com.example.exchangeapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DataDTO {
    private FuturesDTO[] futures;
    private ForexDTO[] forex;
    private StockDTO[] stock;
}
