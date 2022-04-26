package app.model.dto;

import app.model.Future;

import java.math.BigDecimal;
import java.util.Date;

public class FutureDTO extends SecurityDTO{
    private String contractUnit;
    private Date settlementDate;

    public FutureDTO(Future future) {
        super(future);
        this.contractUnit = future.getContractUnit();
        this.settlementDate = future.getSettlementDate();
        this.maintenanceMargin = future.getMaintenanceMargin();
        this.initialMarginCost = maintenanceMargin.multiply(BigDecimal.valueOf(1.1));
    }

}
