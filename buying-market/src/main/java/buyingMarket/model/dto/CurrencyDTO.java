package buyingMarket.model.dto;

import buyingMarket.model.Currency;
import buyingMarket.model.InflationRate;
import lombok.Getter;

import java.util.ArrayList;


@Getter
public class CurrencyDTO {

    private String name;
    private String isoCode;
    private String symbol;
    private String country;
    private ArrayList<InflationRateDTO> inflationRates;

    public CurrencyDTO(Currency currency) {
        this.name = currency.getName();
        this.isoCode = currency.getIsoCode();
        this.symbol = currency.getSymbol();
        this.country = currency.getRegion().getName();

        if(currency.getInflationRates() == null)
            return;

        inflationRates = new ArrayList<>();
        for (InflationRate inflationRate: currency.getInflationRates()){
            inflationRates.add(new InflationRateDTO(inflationRate));
        }
    }
}
