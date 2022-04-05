package com.osiguranje.stocks.controller;

import com.crazzyghost.alphavantage.fundamentaldata.response.CompanyOverviewResponse;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.osiguranje.stocks.model.StockModel;
import com.osiguranje.stocks.services.AlphavantageService;
import com.osiguranje.stocks.services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;
    private final AlphavantageService alphavantageService;

    public StockController(StockService stockService, AlphavantageService alphavantageService) {
        this.stockService = stockService;
        this.alphavantageService = alphavantageService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllStocks(){
        return ResponseEntity.ok(stockService.getStocks());
    }

    @GetMapping(value = "/{stock}")
    public ResponseEntity<?> getStockTimeSeries(@PathVariable("stock") String stock){
        TimeSeriesResponse res = alphavantageService.getTimeSeries(stock);
        CompanyOverviewResponse fundamentalInfo = alphavantageService.getFundamental(stock);
        System.out.println(fundamentalInfo.getOverview().getSharesOutstanding());
        List<StockModel> stockModels = new ArrayList<>();
        for(StockUnit s : res.getStockUnits()){
            stockModels.add(new StockModel(s, stock, fundamentalInfo.getOverview().getSharesOutstanding()));
        }
        stockService.saveAll(stockModels);
        return ResponseEntity.ok(res);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> addStock(StockModel stock){
        return ResponseEntity.ok(stockService.save(stock));
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateStock(StockModel stock){
        return ResponseEntity.ok(stockService.save(stock));
    }
}
