package app.services;

import app.mappers.SecurityMapper;
import app.model.dto.ForexDTO;
import app.model.Forex;
import app.model.dto.SecurityDTO;
import app.repositories.CurrencyRepository;
import app.repositories.ForexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ForexService  {

    private final ForexRepository forexRepository;
    private final CurrencyRepository currencyRepository;
    private final SecurityMapper securityMapper;

    @Autowired
    public ForexService(ForexRepository forexRepository, CurrencyRepository currencyRepository, SecurityMapper securityMapper) {
        this.forexRepository = forexRepository;
        this.currencyRepository = currencyRepository;
        this.securityMapper = securityMapper;
    }

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

    public SecurityDTO findById(long id) {
        Optional<Forex> forex = forexRepository.findById(id);
        return forex.map(securityMapper::forexToSecurityDto).orElse(null);
    }

    public List<Forex> findByTicker(String symbol){
        return forexRepository.findForexByTicker(symbol);
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
