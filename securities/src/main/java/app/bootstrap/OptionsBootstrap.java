package app.bootstrap;

import app.Config;
import app.model.StockOption;
import app.model.OptionType;
import app.model.Stock;
import app.model.api.OptionsAPIResponse;
import app.services.OptionsService;
import app.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class OptionsBootstrap {

    private final StockService stockService;
    private final OptionsService optionsService;

    @Autowired
    public OptionsBootstrap(StockService stockService, OptionsService optionsService) {
        this.stockService = stockService;
        this.optionsService = optionsService;
    }

    public void loadOptionsData(){
        List<Stock> stocks = stockService.getStocksData();
        String url = Config.getProperty("python_service_url");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String lastUpdated = formatter.format(date);

        for(Stock stock: stocks){
            for (OptionType optionType: OptionType.values()){
                RestTemplate rest = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<OptionsAPIResponse> entity = new HttpEntity<>(headers);

                List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
                messageConverters.add(converter);
                rest.setMessageConverters(messageConverters);

                List<HashMap<String, String>> responseContent;
                try {
                    ResponseEntity<OptionsAPIResponse> response = rest.exchange(url + "options/" + optionType + "?symbol=" + stock.getTicker(), HttpMethod.GET, entity, OptionsAPIResponse.class);
                    responseContent = Objects.requireNonNull(response.getBody()).getData();
                } catch (Exception e) {
                    continue;
                }
                for (int i = 0; i <= 3; i++){
                    if(responseContent.size() <= i)
                        break;

                    HashMap<String, String> optionMap = responseContent.get(i);
                    StockOption option;
                    try{
                        option = new StockOption(optionMap.get("Contract Name"), optionMap.get("Contract Name"), stock.getExchange(), lastUpdated,
                                (BigDecimal.valueOf(Double.parseDouble(optionMap.get("Last Price").replace("-", "0")))),
                                (BigDecimal.valueOf(Double.parseDouble(optionMap.get("Ask").replace("-", "0")))),
                                (BigDecimal.valueOf(Double.parseDouble(optionMap.get("Bid").replace("-", "0")))),
                                (BigDecimal.valueOf(Double.parseDouble(optionMap.get("Change").replace("-", "0")))),
                                Long.parseLong(optionMap.get("Volume").replace("-", "1")), 100);
                        option.setStockListing(stock);
                        option.setOptionType(optionType);
                        option.setStrikePrice(BigDecimal.valueOf(Double.parseDouble(optionMap.get("Strike").replace("-", "0"))));
                        option.setImpliedVolatility(BigDecimal.valueOf(Double.parseDouble(optionMap.get("Implied Volatility").replace("%", "").replace("-", "0").replace(",", ""))));
                        option.setOpenInterest(Long.parseLong(optionMap.get("Open Interest").replace("-", "0")));
                        String dateString = "20" + optionMap.get("Contract Name").replace(stock.getTicker(), "").substring(0, 6);
                        Date settlementDate = new SimpleDateFormat("yyyyMMdd").parse(dateString);
                        option.setSettlementDate(settlementDate);
                    } catch (Exception e){
                        continue;
                    }

                    optionsService.save(option);
                }
            }
        }
    }
}
