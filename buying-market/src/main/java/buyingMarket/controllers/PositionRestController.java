package buyingMarket.controllers;

import buyingMarket.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/positions")
public class PositionRestController {
    private final PositionService positionService;

    @Autowired
    public PositionRestController(PositionService positionService){
        this.positionService = positionService;
    }

}
