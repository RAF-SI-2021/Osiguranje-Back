package com.example.exchangeapp.controllers;

import com.example.exchangeapp.models.DataDTO;
import com.example.exchangeapp.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataService dataService;


    @CrossOrigin(origins = "*")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getData(){
        DataDTO data = new DataDTO(dataService.getFuturesData(), dataService.getForexData(),dataService.getStockData());
        return ResponseEntity.ok(data);
    }
}
