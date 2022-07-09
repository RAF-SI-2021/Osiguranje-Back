package app.services;

import app.Config;
import app.model.StockOption;
import app.model.api.OptionsAPIResponse;
import app.model.dto.OptionDTO;
import app.repositories.OptionsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yahoofinance.YahooFinance;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;

    @Value("${api.stockinfo}")
    public String stockinfoApiUrl;

    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    public StockOption save(StockOption option) {
        return optionsRepository.save(option);
    }

    public List <OptionDTO> getOptionsDTOData() {
        List <StockOption> optionList = getOptionsData();
        List <OptionDTO> dtoList = new ArrayList <>();
        for (StockOption o : optionList) {
            dtoList.add(new OptionDTO(o));
        }
        return dtoList;
    }

    public List <StockOption> getOptionsData() {
        return optionsRepository.findAll();
    }

    public List <StockOption> updateData() {
        System.out.println("Updating options");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        List <StockOption> options = getOptionsData();

        for (StockOption o : options) {
            o.setLastUpdated(formatter.format(date));

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <OptionsAPIResponse> entity = new HttpEntity <>(headers);

            List <HttpMessageConverter <?>> messageConverters = new ArrayList <>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(converter);
            rest.setMessageConverters(messageConverters);

            List <HashMap <String, String>> responseContent;
            try {
                ResponseEntity <OptionsAPIResponse> response = rest.exchange(stockinfoApiUrl + "/options/" + o.getOptionType() + "?symbol=" + o.getTicker(), HttpMethod.GET, entity, OptionsAPIResponse.class);
                responseContent = Objects.requireNonNull(response.getBody()).getData();
                if (responseContent.isEmpty())
                    throw new Exception("Empty response");
            } catch (Exception e) {
                continue;
            }

            HashMap <String, String> optionMap = responseContent.get(0);
            try {
                o.setOpenInterest(Long.parseLong(optionMap.get("Open Interest").replace("-", "0")));
                o.setImpliedVolatility(BigDecimal.valueOf(Double.parseDouble(optionMap.get("Implied Volatility").replace("%", "").replace("-", "0").replace(",", ""))));
            } catch (Exception e) {
            }
            optionsRepository.save(o);
        }
        return options;
    }
}
