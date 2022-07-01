package app.model.dto;

import app.model.Forex;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ForexDTO extends SecurityDTO {

    private String baseCurrency;
    private String quoteCurrency;

    public ForexDTO(Forex forex) {
        super(forex);
        this.baseCurrency = forex.getBaseCurrency().getIsoCode();
        this.quoteCurrency = forex.getQuoteCurrency().getIsoCode();
    }

    public ForexDTO() {

    }
    @Override
    public String toString() {
        return "ForexDTO{" + super.toString() +
                "baseCurrency='" + baseCurrency + '\'' +
                ", quoteCurrency='" + quoteCurrency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForexDTO forexDTO = (ForexDTO) o;
        return Objects.equals(baseCurrency, forexDTO.baseCurrency) && Objects.equals(quoteCurrency, forexDTO.quoteCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, quoteCurrency);
    }
}
