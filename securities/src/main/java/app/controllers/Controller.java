package app.controllers;

import app.model.dto.*;
import app.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class Controller {

    private final FuturesService futuresService;
    private final ForexService forexService;
    private final StockService stockService;
    private final OptionsService optionsService;
    private final CurrencyService currencyService;

    private final RedisCacheService redisCacheService;

    @Autowired
    public Controller(FuturesService futuresService, ForexService forexService, StockService stockService, OptionsService optionsService, RedisCacheService redisCacheService,CurrencyService currencyService) {
        this.futuresService = futuresService;
        this.forexService = forexService;
        this.stockService = stockService;
        this.optionsService = optionsService;
        this.redisCacheService = redisCacheService;
        this.currencyService = currencyService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSecurities(){
        DataDTO data = new DataDTO(futuresService.getFutureDTOData(), forexService.getForexDTOData(), stockService.getStocksDTOData());
        return ResponseEntity.ok(data);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOptions(){
        List<OptionDTO> optionsDTOList = optionsService.getOptionsDTOData();
        return ResponseEntity.ok(optionsDTOList);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/forex/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findForexById(@PathVariable long id) {
        ForexDTO dto;

        // Fetch cached forex
        dto = redisCacheService.getForex(String.valueOf(id));
        if (dto != null)
            return ResponseEntity.ok(dto);

        dto = forexService.findById(id);
        if(dto == null)
            return ResponseEntity.notFound().build();
        redisCacheService.saveForex(String.valueOf(id), dto);
        return ResponseEntity.ok(dto);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/futures/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findFutureById(@PathVariable long id) {
        FutureDTO dto = futuresService.findById(id);
        if(dto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/stocks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findStockById(@PathVariable long id) {
        StockDTO dto = stockService.findById(id);
        if(dto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/currency/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findCurrencyById(@PathVariable long id){
        CurrencyDTO dto = currencyService.findById(id);
        if(dto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
}
