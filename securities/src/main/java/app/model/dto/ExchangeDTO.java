package app.model.dto;

import app.model.Currency;
import app.model.Exchange;
import app.model.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExchangeDTO {
    private String name;
    private String acronym;     // TODO
    private String MIC;
    private String country;
    private String timeZone;    /* UTC se podrazumeva, samo "+h" za sad. */
    private String open;
    private String closed;
    private CurrencyDTO currency;

    public ExchangeDTO(Exchange exchange) {
        this();
        if (exchange == null) return;

        this.name = exchange.getName();
        this.acronym = exchange.getAcronym();
        this.MIC = exchange.getMIC();
        this.country = exchange.getRegion().getName();
        this.timeZone = exchange.getTimeZone();
        this.open = exchange.getOpen();
        this.closed = exchange.getClosed();
        this.currency = new CurrencyDTO(exchange.getCurrency());
    }

    public ExchangeDTO() {
        this.name = "NAME";
        this.acronym = "ACRONYM";
        this.MIC = "MIC";
        this.country = "COUNTRY";
        this.timeZone = "TIME_ZONE";
        this.open = "OPEN";
        this.closed = "CLOSED";
        this.currency = new CurrencyDTO(new Currency("CURRENCY_NAME",
                                                    "ISO_CODE",
                                                    "SYMBOL",
                                                    new Region("REGION_NAME", "REGION_CODE")));
    }

    @Override
    public String toString() {
        return "ExchangeDTO{" +
                "name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", MIC='" + MIC + '\'' +
                ", country='" + country + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", open='" + open + '\'' +
                ", closed='" + closed + '\'' +
                ", currency=" + currency +
                '}';
    }
}
