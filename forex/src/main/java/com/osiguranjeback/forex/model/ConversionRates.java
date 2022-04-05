package com.osiguranjeback.forex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConversionRates {
    @JsonProperty("EUR")
    BigDecimal eur;
    @JsonProperty("USD")
    BigDecimal usd;
    @JsonProperty("GBP")
    BigDecimal gbp;
}
