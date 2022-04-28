package app.model.dto;

import app.model.Forex;


public class ForexDTO extends SecurityDTO {

    private String baseCurrency;
    private String quoteCurrency;

    public ForexDTO(Forex forex) {
        super(forex);
        this.baseCurrency = forex.getBaseCurrency().getIsoCode();
        this.quoteCurrency = forex.getQuoteCurrency().getIsoCode();
    }
}
