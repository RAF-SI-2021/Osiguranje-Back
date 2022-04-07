package com.osiguranjeback.forex.services.implementation;

import com.osiguranjeback.forex.model.*;
import com.osiguranjeback.forex.services.ExternalForexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalForexServiceImpl implements ExternalForexService {

    @Value("${external.api.url}")
    private String baseURL;

    @Override
    public List<Forex> getQuotes(String base){
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<ExchangeRateAPIResponse> entity = new HttpEntity<>(headers);
        ResponseEntity<ExchangeRateAPIResponse> response = null;
        response = rest.exchange(baseURL + base, HttpMethod.GET, entity, ExchangeRateAPIResponse.class);
        ConversionRates rates = response.getBody().getConversionRates();
        List<Forex> pairs = new ArrayList<>();

        Forex baseAndEur = new Forex();
        baseAndEur.setBaseCurrency(base);
        baseAndEur.setQuoteCurrency(Currency.EURO.getCode());
        baseAndEur.setPrice(rates.getEur());
        baseAndEur.setContractSize(ContractSize.STANDARD.getSize());
        pairs.add(baseAndEur);

        Forex baseAndUsd = new Forex();
        baseAndUsd.setBaseCurrency(base);
        baseAndUsd.setQuoteCurrency(Currency.DOLLAR.getCode());
        baseAndUsd.setPrice(rates.getUsd());
        baseAndUsd.setContractSize(ContractSize.STANDARD.getSize());
        pairs.add(baseAndUsd);

        Forex baseAndGbp = new Forex();
        baseAndGbp.setBaseCurrency(base);
        baseAndGbp.setQuoteCurrency(Currency.BRITISH_POUND.getCode());
        baseAndGbp.setPrice(rates.getGbp());
        baseAndGbp.setContractSize(ContractSize.STANDARD.getSize());
        pairs.add(baseAndGbp);

        return pairs;

    }
}
