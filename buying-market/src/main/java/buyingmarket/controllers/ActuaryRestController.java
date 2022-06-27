package buyingmarket.controllers;

import buyingmarket.model.dto.ActuaryCreateDto;
import buyingmarket.services.ActuaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/actuaries")
public class ActuaryRestController {
    private ActuaryService actuaryService;

    @Autowired
    public ActuaryRestController(ActuaryService actuaryService) {
        this.actuaryService = actuaryService;
    }

    public ResponseEntity<?> createActuary(@RequestBody ActuaryCreateDto actuary) throws Exception {
        actuaryService.createActuary(actuary);
        return ResponseEntity.noContent().build();
    }
}
