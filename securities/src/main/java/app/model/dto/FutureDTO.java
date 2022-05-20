package app.model.dto;

import app.model.Future;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
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

    @Override
    public String toString() {
        return "FutureDTO{" +
                "contractUnit='" + contractUnit + '\'' +
                ", settlementDate=" + settlementDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FutureDTO futureDTO = (FutureDTO) o;
        return Objects.equals(contractUnit, futureDTO.contractUnit) && Objects.equals(settlementDate, futureDTO.settlementDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contractUnit, settlementDate);
    }
}
