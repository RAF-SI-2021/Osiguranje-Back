package buyingmarket.controllers;

import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.services.ActuaryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> createActuary(@RequestBody ActuaryCreateDto actuary) throws Exception {
        actuaryService.createActuary(actuary);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/usedLimit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setUsedLimit(@PathVariable Long id) {
        actuaryService.resetLimit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/limit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setLimit(@PathVariable Long id, @RequestBody BigDecimal value) {
        actuaryService.setLimit(id, value);
        return ResponseEntity.noContent().build();
    }
}
