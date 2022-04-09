package controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @CrossOrigin(origins = "*")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getData(){
        ArrayList<Security> data = new ArrayList<>();
        //dataService.getFuturesData(), dataService.getForexData(),dataService.getStockData()); todo
        return ResponseEntity.ok(data);
    }
}
