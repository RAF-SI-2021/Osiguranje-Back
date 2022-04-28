package app.bootstrap;

import app.Config;
import app.model.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    public ExchangeBootstrap(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public void loadStockExchangeData()
    {
        try {
            ClassLoader classLoader = ExchangeBootstrap.class.getClassLoader();
            File f = new File(classLoader.getResource(Config.getProperty("exchange_file")).getFile());
            BufferedReader br = new BufferedReader(new FileReader(f));

            String line;
            while( ( line = br.readLine() ) != null ) {
                String[] columns = line.split( "," );
                Exchange stockExchange = new Exchange( columns[2], null, columns[4], columns[1], columns[5], columns[3], columns[0] );

//                Optional<Currency> countryCurrency = currencyRepository.findByRegion( columns[1] );
//                if( countryCurrency.isPresent() && !currencies.contains( countryCurrency.get() ) ) currencies.add( countryCurrency.get() );
//
//                stockExchange.setCurrencies( currencies );
                exchangeRepository.save(stockExchange);
            }

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
