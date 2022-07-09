package app.services;

import app.model.Currency;
import app.model.Future;
import app.model.dto.CurrencyDTO;
import app.model.dto.FutureDTO;
import app.repositories.CurrencyRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public CurrencyDTO findById(long id) {
        Optional<Currency> opFuture = currencyRepository.findById(id);
        if(opFuture.isEmpty())
            return null;
        Currency currency = opFuture.get();
        return new CurrencyDTO(currency);
    }

    public List<CurrencyDTO> getAllCurrency(){
        return currencyRepository.findAll().stream().map(c -> new CurrencyDTO(c)).collect(Collectors.toList());
    }

}
