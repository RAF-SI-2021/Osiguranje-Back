package services;

import model.Forex;
import repositories.ForexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForexService  {

    @Autowired
    private ForexRepository forexRepository;

    public void save(Forex forex){
        forexRepository.save(forex);
    }

    public void saveAll(List<Forex> pairs) {
        forexRepository.saveAll(pairs);
    }

    public List<Forex> getForexData() {
        return forexRepository.findAll();
    }

    public List<Forex> findBySymbol(String symbol){
        return forexRepository.findForexBySymbol(symbol);
    }

    public Forex getPair(String baseCurrency, String quoteCurrency) {
        if(baseCurrency != null && quoteCurrency != null) {
            return forexRepository.findByBaseCurrencyAndQuoteCurrency(baseCurrency, quoteCurrency).orElse(null);
        }
        return null;
    }

}
