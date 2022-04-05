package futures.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
public class FuturesContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String symbol;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer contractSize;
    @Column(nullable = false)
    private String contractUnit;
    @Column(nullable = false)
    private Integer maintenanceMargin;
    @Column(nullable = false)
    private Date settlementDate;

    public FuturesContract(){}

    public FuturesContract(String symbol, String productName, Integer contractSize, String contractUnit, Integer maintenanceMargin, Date settlementDate) {
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
