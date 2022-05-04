package app.bootstrap;

import app.Config;
import app.model.Currency;
import app.model.Exchange;
import app.model.Region;
import app.repositories.CurrencyRepository;
import app.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import app.repositories.ExchangeRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Component
public class ExchangeBootstrap {

//    @Value("${stock_exchange}")
//    private String stockExchangeCSVPath;

    private final ExchangeRepository exchangeRepository;
    private final CurrencyRepository currencyRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public ExchangeBootstrap(ExchangeRepository exchangeRepository, CurrencyRepository currencyRepository, RegionRepository regionRepository) {
        this.exchangeRepository = exchangeRepository;
        this.currencyRepository = currencyRepository;
        this.regionRepository = regionRepository;
    }

    public void loadStockExchangeData()
    {
        ClassLoader classLoader = ExchangeBootstrap.class.getClassLoader();
        File f = new File(classLoader.getResource(Config.getProperty("exchange_file")).getFile());

        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] columns = line.split( "," );
                Exchange stockExchange = new Exchange(columns[2], columns[6], columns[4], columns[5], columns[3], columns[0] );

                Region region = regionRepository.findByName(columns[1]);
                if (region == null)
                    region = regionRepository.findByCode("EUR");
                Currency cur = currencyRepository.findByRegion(region);
                if (cur == null)
                    cur = currencyRepository.findByIsoCode("EUR");

                stockExchange.setCurrency(cur);
                stockExchange.setRegion(region);

                exchangeRepository.save(stockExchange);
            }

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
