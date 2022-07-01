package app.controller;

import app.model.*;
import app.model.dto.*;
import app.services.CompanyService;
import app.services.ContractService;
import app.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;
    private final CompanyService companyService;
    private final TransactionService transactionService;

    @Autowired
    public ContractController(ContractService contractService, CompanyService companyService, TransactionService transactionService) {
        this.contractService = contractService;
        this.companyService = companyService;
        this.transactionService = transactionService;
    }

    // todo autorizacija

    @CrossOrigin(origins = "*")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getContracts(){
        List<ContractDTO> data = ListMapper.contractToContractDTO(contractService.findAll());
        return ResponseEntity.ok(data);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getContract(@NotNull @PathVariable Long id){
        Optional<Contract> optionalContract = contractService.findByID(id);
        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();

        Contract contract = optionalContract.get();
        return ResponseEntity.ok(contract);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createContract(@RequestBody CreateContractDTO createContractDTO) {
        Optional<Company> optionalCompany = companyService.findByID(createContractDTO.getCompanyID());
        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();
        Company company = optionalCompany.get();

        Contract contract = new Contract(createContractDTO);
        contract.setCompany(company);
        contractService.save(contract);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{id}/finalize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> finalizeContract(@NotNull @PathVariable Long id) {
        Optional<Contract> optionalContract = contractService.findByID(id);

        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();

        // todo account

        contractService.finalize(contract);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{id}/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createTransaction(@NotNull @PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        Optional<Contract> optionalContract = contractService.findByID(id);

        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();

        if (contract.getStatus().equals(Status.FINALIZED))
            return ResponseEntity.badRequest().build();

        // todo update accounts

        Transaction transaction = new Transaction(transactionDTO);
        contractService.createTransaction(contract, transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{contractID}/transactions/{transactionID}")
    public ResponseEntity<?> deleteTransaction(@NotNull @PathVariable Long contractID, @NotNull @PathVariable Long transactionID) {
        Optional<Contract> optionalContract = contractService.findByID(contractID);

        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();

        if (contract.getStatus().equals(Status.FINALIZED))
            return ResponseEntity.badRequest().build();

        Optional<Transaction> optionalTransaction = transactionService.findByID(transactionID);

        if(optionalTransaction.isEmpty())
            return ResponseEntity.badRequest().build();

        // todo account

        contractService.deleteTransaction(contract, transactionID);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/{contractID}/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTransaction(@NotNull @PathVariable Long contractID, @RequestBody TransactionDTO transactionDTO) {
        Optional<Contract> optionalContract = contractService.findByID(contractID);
        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();
        if (contract.getStatus().equals(Status.FINALIZED))
            return ResponseEntity.badRequest().build();

        Optional<Transaction> optionalTransaction = transactionService.findByID(transactionDTO.getId());
        if(optionalTransaction.isEmpty())
            return ResponseEntity.badRequest().build();

        // todo account

        contractService.updateTransaction(contract, transactionDTO);
        return ResponseEntity.ok().build();
    }

}
