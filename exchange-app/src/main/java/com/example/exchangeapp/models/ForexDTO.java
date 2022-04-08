package com.example.exchangeapp.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ForexDTO {
    private Long id;
    private String baseCurrency;
    private String quoteCurrency;
    private Integer contractSize;
    private BigDecimal price;

    public BigDecimal nominalValue(BigDecimal lotSize) {
        return lotSize.multiply(price);
    }
}
