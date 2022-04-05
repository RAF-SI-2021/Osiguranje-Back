package com.osiguranjeback.forex;

import com.osiguranjeback.forex.model.Currency;
import com.osiguranjeback.forex.model.Forex;
import com.osiguranjeback.forex.services.ExternalForexService;
import com.osiguranjeback.forex.services.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ForexApplication implements CommandLineRunner {

    @Autowired
    private ExternalForexService externalForexService;
    @Autowired
    private ForexService forexService;

    public static void main(String[] args) {
        SpringApplication.run(ForexApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for(Currency currency: Currency.values()){
            List<Forex> quotes = externalForexService.getQuotes(currency.getCode());
            forexService.saveAll(quotes);
        }

    }
}
