package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
public class Future extends Security{

    @Column(nullable = false)
    private Integer contractSize;
    @Column(nullable = false)
    private String contractUnit;
    @Column(nullable = false)
    private Integer maintenanceMargin;
    @Column(nullable = false)
    private Date settlementDate;

    public Future(){}

    public Future(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume, Integer contractSize, String contractUnit, Integer maintenanceMargin, Date settlementDate) {
        super(symbol, description, null, lastUpdated, price, ask, bid, priceChange, volume);
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
