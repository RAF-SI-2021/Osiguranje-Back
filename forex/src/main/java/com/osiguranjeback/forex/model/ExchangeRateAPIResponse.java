package com.osiguranjeback.forex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExchangeRateAPIResponse {
    @JsonProperty("conversion_rates")
    ConversionRates conversionRates;
}
