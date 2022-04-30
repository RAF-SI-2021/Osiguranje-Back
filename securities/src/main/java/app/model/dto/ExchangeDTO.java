package app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import app.model.Exchange;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        this.name = exchange.getName();
        this.acronym = exchange.getAcronym();
        this.MIC = exchange.getMIC();
        this.country = exchange.getCountry();
        this.timeZone = exchange.getTimeZone();
        this.open = exchange.getOpen();
        this.closed = exchange.getClosed();
        this.currency = new CurrencyDTO(exchange.getCurrency());
    }
}
