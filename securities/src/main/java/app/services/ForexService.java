package app.services;

import app.Config;
import app.model.Currency;
import app.model.api.ExchangeRateAPIResponse;
import app.model.dto.ForexDTO;
import app.model.Forex;
import app.repositories.CurrencyRepository;
import app.repositories.ForexRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Setter
public class ForexService  {

    private Date lastupdated = new Date();

    private final ForexRepository forexRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public ForexService(ForexRepository forexRepository, CurrencyRepository currencyRepository) {
        this.forexRepository = forexRepository;
        this.currencyRepository = currencyRepository;
    }

    public void save(Forex forex){
        forexRepository.save(forex);
    }

    public void saveAll(List<Forex> pairs) {
        forexRepository.saveAll(pairs);
    }

    public List<ForexDTO> getForexDTOData(){
        List<Forex> forexList = getForexData();
        List<ForexDTO> dtoList = new ArrayList<>();
        for (Forex f: forexList){
            dtoList.add(new ForexDTO(f));
        }
        return dtoList;
    }

    public Forex findByTicker(String symbol){
        return forexRepository.findForexByTicker(symbol);
    }

    public ForexDTO findById(long id) {
        Optional<Forex> opForex = forexRepository.findById(id);
        if(!opForex.isPresent())
            return null;
        Forex forex = opForex.get();
        ForexDTO dto = new ForexDTO(forex);
        return dto;
    }

    private List<Forex> getForexData() {
        Date currDate = new Date();
        long timeDifference = currDate.getTime() - lastupdated.getTime();
        long minutesDifference = timeDifference / 60000;
        List<Forex> forexList = forexRepository.findAll();
        if(minutesDifference > 15) {
            System.out.println("Updating forex data");
            try {
                return updateData(forexList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return forexList;
    }

    private List<Forex> updateData(List<Forex> forexList) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        setLastupdated(date);

        List<Currency> currencies = currencyRepository.findAll();
        for (Currency currency : currencies) {
            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ExchangeRateAPIResponse> entity = new HttpEntity<>(headers);
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
                Forex f = forexRepository.findForexByTicker(symbol);
                if (f == null)
                    continue;

                f.setPrice(rates.get(currency2.getIsoCode()));
                f.setLastUpdated(formatter.format(date));
                f.setAsk(rates.get(currency2.getIsoCode()));
                f.setBid(rates.get(currency2.getIsoCode()));
                f.setPriceChange(f.getBid().subtract(f.getAsk()));
                f.setVolume(null);
                forexRepository.save(f);
            }
        }
        return forexList;
    }

//    public Forex getPair(String baseCurrencyIso, String quoteCurrencyIso) {
//        Currency baseCurrency = currencyRepository.findByIsoCode(baseCurrencyIso);
//        Currency quoteCurrency = currencyRepository.findByIsoCode(quoteCurrencyIso);
//
//        if(baseCurrency != null && quoteCurrency != null) {
//            return forexRepository.findByBaseCurrencyAndQuoteCurrency(baseCurrency, quoteCurrency).orElse(null);
//        }
//        return null;
//    }

}
