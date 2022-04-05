package com.osiguranje.stocks.controller;

import com.osiguranje.stocks.model.StockModel;
import com.osiguranje.stocks.services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllStocks(){
        return ResponseEntity.ok(stockService.getStocks());
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
