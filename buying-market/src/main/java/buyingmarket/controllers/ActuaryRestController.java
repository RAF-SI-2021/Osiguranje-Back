package buyingmarket.controllers;

import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.services.ActuaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin
@RestController
@RequestMapping("/api/actuaries")
public class ActuaryRestController {
    private final ActuaryService actuaryService;

    @Autowired
    public ActuaryRestController(ActuaryService actuaryService) {
        this.actuaryService = actuaryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createActuary(@RequestBody ActuaryCreateDto actuary) {
        actuaryService.createActuary(actuary);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/usedLimit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> setUsedLimit(@PathVariable Long id) {
        actuaryService.resetLimit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/limit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> setLimit(@PathVariable Long id, @RequestBody BigDecimal value) {
        actuaryService.setLimit(id, value);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/limit/update/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLimit(@PathVariable Long id, @RequestBody BigDecimal value){
        try {
            actuaryService.changeLimit(id,value);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
