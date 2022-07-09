package app.bootstrap;

import app.Config;
import app.model.Exchange;
import app.model.Forex;
import app.model.ContractSize;
import app.model.Currency;
import app.model.api.ExchangeRateAPIResponse;
import app.repositories.ExchangeRepository;
import app.services.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import app.repositories.CurrencyRepository;
import app.repositories.ForexRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ForexBootstrap {

    private final ForexRepository forexRepository;
    private final CurrencyRepository currencyRepository;
    private final ForexService forexService;
    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ForexBootstrap(ForexRepository forexRepository, CurrencyRepository currencyRepository, ForexService forexService, ExchangeRepository exchangeRepository) {
        this.forexRepository = forexRepository;
        this.currencyRepository = currencyRepository;
        this.forexService = forexService;
        this.exchangeRepository = exchangeRepository;
    }

    public void loadForexData() {
        List<Currency> currencies = currencyRepository.findAll();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        for (Currency currency : currencies) {

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ExchangeRateAPIResponse> entity = new HttpEntity <>(headers);
            ResponseEntity<ExchangeRateAPIResponse> response = rest.exchange(Config.getProperty("exchangerate_url") + currency.getIsoCode(), HttpMethod.GET, entity, ExchangeRateAPIResponse.class);
            HashMap<String, BigDecimal> rates;
            try {
                rates = Objects.requireNonNull(response.getBody()).getConversionRates();
            } catch (Exception e) {
                continue;
            }
            for (Currency currency2 : currencies) {
                if (currency2.equals(currency))
                    continue;

                String symbol = currency.getIsoCode() + currency2.getIsoCode();
                Forex forexExists = forexRepository.findForexByTicker(symbol);
                if (forexExists != null) {
                    continue;
                }
                try {
                    BigDecimal price = rates.get(currency2.getIsoCode());
                    String lastUpdated = formatter.format(date);
                    BigDecimal ask = price;
                    BigDecimal bid = price;
                    BigDecimal priceChange = bid.subtract(ask);
                    Long volume = null;

                    Forex newForex = new Forex(symbol, symbol, lastUpdated, price, ask, bid, priceChange, volume, ContractSize.MICRO.getSize());
                    newForex.setBaseCurrency(currency);
                    newForex.setQuoteCurrency(currency2);

                    Exchange stockExchange = exchangeRepository.findByAcronym("FXCM");
                    newForex.setExchange(stockExchange);
                    newForex.setSecurityHistory(null);

                    forexRepository.save(newForex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
