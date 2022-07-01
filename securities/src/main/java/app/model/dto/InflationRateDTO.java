package app.model.dto;

import app.model.InflationRate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class InflationRateDTO {
    private Date date;
    private Float value;

    public InflationRateDTO(InflationRate inflationRate) {
        this.date = inflationRate.getDate();
        this.value = inflationRate.getInflationValue();
    }
}
