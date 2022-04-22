package controllers;

import model.dto.DataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ForexService;
import services.FuturesService;
import services.StockService;

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
        DataDTO data = new DataDTO(futuresService.getFutureDTOData(), forexService.getForexDTOData(),stockService.getStocksDTOData());
        return ResponseEntity.ok(data);
    }
}
