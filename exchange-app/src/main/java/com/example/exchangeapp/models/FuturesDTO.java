package com.example.exchangeapp.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class FuturesDTO {
    private Long id;
    private String symbol;
    private String productName;
    private Integer contractSize;
    private String contractUnit;
    private Integer maintenanceMargin;
    private Date settlementDate;

    public FuturesDTO(){}

    public FuturesDTO(String symbol, String productName, Integer contractSize, String contractUnit, Integer maintenanceMargin, Date settlementDate) {
        this.symbol = symbol;
        this.productName = productName;
        this.contractSize = contractSize;
        this.contractUnit = contractUnit;
        this.maintenanceMargin = maintenanceMargin;
        this.settlementDate = settlementDate;
    }

    public Double getNominalValue(Double price) {
        return contractSize * price;
    }

    public Double getInitialMarginCost() {
        return maintenanceMargin * 1.1;
    }
}
