package com.osiguranjeback.forex.controllers;

import com.osiguranjeback.forex.model.Forex;
import com.osiguranjeback.forex.services.ForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${forex.basedir}")
public class ForexController {

    @Autowired
    private ForexService service;

    @GetMapping("/pair")
    public ResponseEntity<Forex> getPair(@RequestParam String baseCurrency, @RequestParam String quoteCurrency){
        Forex forex = service.getPair(baseCurrency, quoteCurrency);
        if(forex == null) {
            return new ResponseEntity<Forex>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Forex>(forex, HttpStatus.OK);
    }

    @GetMapping(path="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(service.getAll());
    }
}
