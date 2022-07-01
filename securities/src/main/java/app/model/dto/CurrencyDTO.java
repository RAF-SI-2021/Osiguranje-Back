package app.model.dto;

import lombok.Getter;
import app.model.Currency;
import app.model.InflationRate;

import java.util.ArrayList;


@Getter
public class CurrencyDTO {

    private String name;
    private String isoCode;
    private String symbol;
    private String country;
    private ArrayList<InflationRateDTO> inflationRates;

    public CurrencyDTO () {

    }
    public CurrencyDTO(Currency currency) {
        this.name = currency.getName();
        this.isoCode = currency.getIsoCode();
        this.symbol = currency.getSymbol();
        this.country = currency.getRegion().getName();

        if(currency.getInflationRates() == null || currency.getInflationRates().isEmpty())
            return;

        inflationRates = new ArrayList<>();
        for (InflationRate inflationRate: currency.getInflationRates()){
            inflationRates.add(new InflationRateDTO(inflationRate));
        }
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "name='" + name + '\'' +
                ", isoCode='" + isoCode + '\'' +
                ", symbol='" + symbol + '\'' +
                ", country='" + country + '\'' +
                ", inflationRates=" + inflationRates +
                '}';
    }
}
