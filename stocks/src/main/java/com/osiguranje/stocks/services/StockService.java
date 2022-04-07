package com.osiguranje.stocks.services;

import com.osiguranje.stocks.model.StockModel;
import com.osiguranje.stocks.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockModel> getStocks(){
        return stockRepository.findAll();
    }

    public StockModel save(StockModel stock){
        return stockRepository.save(stock);
    }

    public List<StockModel> saveAll(List<StockModel> stocks){
        for(StockModel stock : stocks) {
            stockRepository.save(stock);
        }
        return stocks;
    }
}
