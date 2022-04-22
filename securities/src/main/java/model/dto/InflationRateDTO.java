package model.dto;

import model.InflationRate;

public class InflationRateDTO {
    private Integer year;
    private Float value;

    public InflationRateDTO(InflationRate inflationRate) {
        this.year = inflationRate.getYear();
        this.value = inflationRate.getValue();
    }
}
