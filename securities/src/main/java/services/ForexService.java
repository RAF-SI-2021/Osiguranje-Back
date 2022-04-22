package services;

import model.dto.ForexDTO;
import model.Forex;
import repositories.CurrencyRepository;
import repositories.ForexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForexService  {

    @Autowired
    private ForexRepository forexRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    public void save(Forex forex){
        forexRepository.save(forex);
    }

    public void saveAll(List<Forex> pairs) {
        forexRepository.saveAll(pairs);
    }

    public List<Forex> getForexData() {
        return forexRepository.findAll();
    }

    public List<ForexDTO> getForexDTOData(){
        List<Forex> forexList = getForexData();
        List<ForexDTO> dtoList = new ArrayList<>();
        for (Forex f: forexList){
            dtoList.add(new ForexDTO(f));
        }
        return dtoList;
    }

    public List<Forex> findBySymbol(String symbol){
        return forexRepository.findForexBySymbol(symbol);
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
