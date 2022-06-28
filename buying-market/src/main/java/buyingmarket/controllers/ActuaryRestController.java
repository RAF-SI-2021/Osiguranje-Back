package buyingmarket.controllers;

import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.services.ActuaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/actuaries")
public class ActuaryRestController {
    private ActuaryService actuaryService;

    @Autowired
    public ActuaryRestController(ActuaryService actuaryService) {
        this.actuaryService = actuaryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createActuary(@RequestBody ActuaryCreateDto actuary) throws Exception {
        actuaryService.createActuary(actuary);
        return ResponseEntity.noContent().build();
    }
}
