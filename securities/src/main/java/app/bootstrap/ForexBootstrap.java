package app.bootstrap;

import app.model.Forex;
import app.model.ContractSize;
import app.model.Currency;
import app.model.api.ExchangeRateAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class ForexBootstrap {

    @Value("${exchangerate.api.url}")
    private String exchangeRateUrl;

//    @Value("${currencies}")
//    private String currenciesPath;

    private final ForexRepository forexRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public ForexBootstrap(ForexRepository forexRepository, CurrencyRepository currencyRepository) {
        this.forexRepository = forexRepository;
        this.currencyRepository = currencyRepository;
    }

    public void loadForexData() {
        List<Currency> currencies = currencyRepository.findAll();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        for (Currency currency : currencies) {

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ExchangeRateAPIResponse> entity = new HttpEntity <>(headers);
            ResponseEntity<ExchangeRateAPIResponse> response = rest.exchange(exchangeRateUrl + currency.getIsoCode(), HttpMethod.GET, entity, ExchangeRateAPIResponse.class);
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
                List <Forex> forexExists = forexRepository.findForexByTicker(symbol);
                if (!forexExists.isEmpty()) {
                    continue;
                }
                try {
                    BigDecimal price = rates.get(currency2.getIsoCode());
                    String lastUpdated = formatter.format(date);
                    BigDecimal ask = price;
                    BigDecimal bid = price;
                    BigDecimal priceChange = price;
                    Long volume = price.longValue();

                    Forex newForex = new Forex(symbol, symbol, lastUpdated, price, ask, bid, priceChange, volume, ContractSize.STANDARD.getSize());
                    newForex.setBaseCurrency(currency);
                    newForex.setQuoteCurrency(currency2);
                    newForex.setSecurityHistory(null);
                    forexRepository.save(newForex);
                } catch (Exception e) {
                }
            }
        }
    }
}
