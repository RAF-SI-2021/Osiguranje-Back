package app.services;

import app.Config;
import app.model.Option;
import app.model.Stock;
import app.model.api.OptionsAPIResponse;
import app.model.dto.OptionDTO;
import app.repositories.OptionsRepository;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;
    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    public Option save(Option option){
        return optionsRepository.save(option);
    }

    public List<OptionDTO> getOptionsDTOData(){
        List<Option> optionList = getOptionsData();
        List<OptionDTO> dtoList = new ArrayList<>();
        for (Option o: optionList){
            dtoList.add(new OptionDTO(o));
        }
        return dtoList;
    }

    public List<Option> getOptionsData() {
        return optionsRepository.findAll();
    }

    public List<Option> updateData() {
        System.out.println("Updating options");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        List<Option> options = getOptionsData();

        for (Option o: options){
            o.setLastUpdated(formatter.format(date));

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<OptionsAPIResponse> entity = new HttpEntity<>(headers);

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            rest.setMessageConverters(messageConverters);

            List<HashMap<String, String>> responseContent;
            try {
                ResponseEntity<OptionsAPIResponse> response = rest.exchange(Config.getProperty("python_service_url") + "options/" + o.getOptionType() + "?symbol=" + o.getTicker(), HttpMethod.GET, entity, OptionsAPIResponse.class);
                responseContent = Objects.requireNonNull(response.getBody()).getData();
            } catch (Exception e) {
                continue;
            }

            if(responseContent.isEmpty())
                continue;

            HashMap<String, String> optionMap = responseContent.get(0);
            try{
                o.setOpenInterest(Long.parseLong(optionMap.get("Open Interest").replace("-", "0")));
                o.setImpliedVolatility(BigDecimal.valueOf(Double.parseDouble(optionMap.get("Implied Volatility").replace("%", "").replace("-", "0").replace(",", ""))));
            } catch (Exception e){}
            optionsRepository.save(o);
        }
        return options;
    }
}
