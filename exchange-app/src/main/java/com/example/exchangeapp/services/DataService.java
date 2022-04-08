package com.example.exchangeapp.services;

import com.example.exchangeapp.models.ForexDTO;
import com.example.exchangeapp.models.FuturesDTO;
import com.example.exchangeapp.models.StockDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DataService {

    private final RestTemplate restTemplate;

    public DataService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ForexDTO[] getForexData(){
        return restTemplate.getForObject("http://localhost:8210/api/v1/forex/all", ForexDTO[].class);
    }
    public FuturesDTO[] getFuturesData(){
        return restTemplate.getForObject("http://localhost:2000/api/futures/data",FuturesDTO[].class);
    }
    public StockDTO[] getStockData(){
        return restTemplate.getForObject("http://localhost:6000/api/stocks/all",StockDTO[].class);
    }
}
