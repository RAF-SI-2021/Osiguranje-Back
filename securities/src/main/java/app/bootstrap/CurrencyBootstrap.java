package app.bootstrap;

import app.Config;
import app.model.Currency;
import app.model.InflationRate;
import app.model.Region;
import app.model.api.InflationRateAPIResponse;
import app.repositories.CurrencyRepository;
import app.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Component
public class CurrencyBootstrap {
    private final CurrencyRepository currencyRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public CurrencyBootstrap(CurrencyRepository currencyRepository, RegionRepository regionRepository) {
        this.currencyRepository = currencyRepository;
        this.regionRepository = regionRepository;
    }

    public void loadCurrenciesData() throws IOException {
        loadRegions();

        ClassLoader classLoader = CurrencyBootstrap.class.getClassLoader();
        InputStream file = new ClassPathResource(Config.getProperty("currencies_file")).getInputStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if(columns.length < 4)
                    continue;
                Region region = regionRepository.findByCode(columns[0]);
                Currency currency = new Currency(columns[2], columns[1], columns[3], region);

                RestTemplate rest = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<InflationRateAPIResponse> entity = new HttpEntity <>(headers);
                ResponseEntity<InflationRateAPIResponse> response = rest.exchange(Config.getProperty("nasdaq_inflation_rate_url") + region.getCode() + ".json?api_key=" + Config.getProperty("nasdaq_api_key") + "&start_date=2019-01-01", HttpMethod.GET, entity, InflationRateAPIResponse.class);
                ArrayList<ArrayList<String>> data;
                ArrayList<InflationRate> inflationRates = new ArrayList<>();
                try {
                    data = Objects.requireNonNull(response.getBody()).getDataset().getData();

                    for (ArrayList<String> pair: data){
                        String dateString = pair.get(0);
                        String valueString = pair.get(1);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
                        Date date = formatter.parse(dateString);
                        Float value = Float.valueOf(valueString);
                        InflationRate inflationRate = new InflationRate(currency, date, value);
                        inflationRates.add(inflationRate);
                    }
                } catch (Exception e) {}

                currency.setInflationRates(inflationRates);
                currencyRepository.save(currency);
            }

        } catch (IOException e) {}
    }

    private void loadRegions() throws IOException {
        ClassLoader classLoader = CurrencyBootstrap.class.getClassLoader();
        InputStream file = new ClassPathResource(Config.getProperty("regions_file")).getInputStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if(columns.length < 2)
                    continue;
                Region region = new Region(columns[1], columns[0]);
                regionRepository.save(region);
            }
        } catch (IOException e) {
        }
    }
}
