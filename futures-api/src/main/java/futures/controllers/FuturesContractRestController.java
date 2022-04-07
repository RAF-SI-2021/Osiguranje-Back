package futures.controllers;

import futures.model.FuturesContract;
import futures.services.FuturesContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/futures")
public class FuturesContractRestController {
    private final FuturesContractService futuresContractService;

    @Autowired
    public FuturesContractRestController(FuturesContractService futuresContractService) {
        this.futuresContractService = futuresContractService;
    }

    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFuturesContractData() {
        return ResponseEntity.ok(futuresContractService.getFuturesContractData());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFuturesContract(FuturesContract futuresContract) {
        return ResponseEntity.ok(futuresContractService.save(futuresContract));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFuturesContract(FuturesContract futuresContract) {
        return ResponseEntity.ok(futuresContractService.save(futuresContract));
    }
}
