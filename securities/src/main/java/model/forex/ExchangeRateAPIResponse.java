package model.forex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExchangeRateAPIResponse {
    @JsonProperty("conversion_rates")
    HashMap<String, BigDecimal> conversionRates;
}
