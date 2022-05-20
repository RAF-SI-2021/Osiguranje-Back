package app.model;

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
    private String contractUnit;
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal maintenanceMargin;
    @Column(nullable = false)
    private Date settlementDate;

    public Future(){}

    public Future(String symbol, String description, String lastUpdated, BigDecimal price, BigDecimal ask, BigDecimal bid, BigDecimal priceChange, Long volume, Integer contractSize, String contractUnit, Integer maintenanceMargin, Date settlementDate) {
        super(symbol, description, null, lastUpdated, price, ask, bid, priceChange, volume, contractSize);
        this.contractUnit = contractUnit;
        this.maintenanceMargin = BigDecimal.valueOf(maintenanceMargin);
        this.settlementDate = settlementDate;
    }
}
