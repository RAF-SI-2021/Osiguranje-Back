package com.osiguranje.stocks.controller;

import com.crazzyghost.alphavantage.fundamentaldata.response.CompanyOverviewResponse;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.osiguranje.stocks.model.StockModel;
import com.osiguranje.stocks.services.AlphavantageService;
import com.osiguranje.stocks.services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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

    @GetMapping(value = "/{start}:{end}")
    public ResponseEntity<?> getStockTimeSeries(@PathVariable("start") String start, @PathVariable("end") String end){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = new Date(sdf.parse(start).getTime());
            Date endDate = new Date(sdf.parse(end).getTime());
            List<StockModel> res = stockService.findByDateWindow(startDate, endDate);
            return ResponseEntity.ok(res);
        }catch (ParseException e){
            return ResponseEntity.ok(e.getMessage());
        }
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
