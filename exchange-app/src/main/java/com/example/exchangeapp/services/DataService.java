package com.example.exchangeapp.services;

import com.example.exchangeapp.config.ApiConfig;
import com.example.exchangeapp.models.ForexDTO;
import com.example.exchangeapp.models.FuturesDTO;
import com.example.exchangeapp.models.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataService {

    private final RestTemplate restTemplate;

    @Autowired
    private ApiConfig apiConfig;

    public DataService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public ForexDTO[] getForexData(){
        return restTemplate.getForObject(apiConfig.getForexApi(), ForexDTO[].class);
    }
    public FuturesDTO[] getFuturesData(){
        return restTemplate.getForObject(apiConfig.getFuturesApi(),FuturesDTO[].class);
    }
    public StockDTO[] getStockData(){
        return restTemplate.getForObject(apiConfig.getStocksApi(),StockDTO[].class);
    }
}
