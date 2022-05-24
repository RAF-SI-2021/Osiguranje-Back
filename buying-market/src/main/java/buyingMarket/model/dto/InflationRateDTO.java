package buyingMarket.model.dto;


import buyingMarket.model.InflationRate;

import java.util.Date;

public class InflationRateDTO {
    private Date date;
    private Float value;

    public InflationRateDTO(InflationRate inflationRate) {
        this.date = inflationRate.getDate();
        this.value = inflationRate.getValue();
    }
}
