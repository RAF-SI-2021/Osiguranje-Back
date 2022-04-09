package controllers;

import model.Future;
import services.FuturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/futures")
public class FuturesContractRestController {
    private final FuturesService futuresContractService;

    @Autowired
    public FuturesContractRestController(FuturesService futuresContractService) {
        this.futuresContractService = futuresContractService;
    }

    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFuturesContractData() {
        return ResponseEntity.ok(futuresContractService.getFuturesData());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFuturesContract(Future futuresContract) {
        return ResponseEntity.ok(futuresContractService.save(futuresContract));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFuturesContract(Future futuresContract) {
        return ResponseEntity.ok(futuresContractService.save(futuresContract));
    }
}
