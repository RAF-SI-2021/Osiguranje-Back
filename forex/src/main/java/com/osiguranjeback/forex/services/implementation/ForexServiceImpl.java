package com.osiguranjeback.forex.services.implementation;

import com.osiguranjeback.forex.model.Forex;
import com.osiguranjeback.forex.repositories.ForexRepository;
import com.osiguranjeback.forex.services.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForexServiceImpl implements ForexService {
    @Autowired
    private ForexRepository forexRepository;
    @Override
    public void save(Forex forex){
        forexRepository.save(forex);
    }

    @Override
    public Forex getPair(String baseCurrency, String quoteCurrency) {
        if(baseCurrency != null && quoteCurrency != null) {
            return forexRepository.findByBaseCurrencyAndQuoteCurrency(baseCurrency, quoteCurrency).orElse(null);
        }
        return null;
    }

    @Override
    public void saveAll(List<Forex> pairs) {
        forexRepository.saveAll(pairs);
    }


}
