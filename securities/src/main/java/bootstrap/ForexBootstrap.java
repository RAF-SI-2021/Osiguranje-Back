package bootstrap;

import model.Forex;
import model.forex.ContractSize;
import model.Currency;
import model.forex.ExchangeRateAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import repositories.CurrencyRepository;
import repositories.ForexRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    @Value("${currencies}")
    private String currenciesPath;

    private final ForexRepository forexRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public ForexBootstrap(ForexRepository forexRepository, CurrencyRepository currencyRepository) {
        this.forexRepository = forexRepository;
        this.currencyRepository = currencyRepository;
    }

    public void loadForexData() {
        readCurrencies();
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
                List <Forex> forexExists = forexRepository.findForexBySymbol(symbol);
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

    private void readCurrencies() {
        try (BufferedReader br = new BufferedReader(new FileReader(currenciesPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                Currency currency = new Currency(columns[2], columns[1], columns[3], columns[0]);
                currencyRepository.save(currency);
            }

        } catch (IOException e) {
        }
    }
}
