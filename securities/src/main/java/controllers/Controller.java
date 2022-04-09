package controllers;

import model.DataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repositories.CurrencyRepository;
import repositories.SecurityHistoryRepository;
import services.ForexService;
import services.FuturesService;
import services.StockService;

import java.security.Security;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/data")
public class Controller {

    private final FuturesService futuresService;
    private final ForexService forexService;
    private final StockService stockService;

    @Autowired
    public Controller(FuturesService futuresService, ForexService forexService, StockService stockService) {
        this.futuresService = futuresService;
        this.forexService = forexService;
        this.stockService = stockService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getData(){
        DataDTO data = new DataDTO(futuresService.getFuturesData(), forexService.getForexData(),stockService.getStocksData());
        return ResponseEntity.ok(data);
    }
}
