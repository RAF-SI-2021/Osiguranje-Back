package app.bootstrap;

import app.Config;
import app.model.Currency;
import app.model.Exchange;
import app.model.Region;
import app.repositories.CurrencyRepository;
import app.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import app.repositories.ExchangeRepository;

import java.io.*;

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

    public void loadStockExchangeData() throws IOException {
        ClassLoader classLoader = ExchangeBootstrap.class.getClassLoader();
        InputStream f = new ClassPathResource(Config.getProperty("exchange_file")).getInputStream();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(f))) {
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
