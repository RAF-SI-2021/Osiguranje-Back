package app.controller;

import app.Config;
import app.model.*;
import app.model.api.BalanceUpdateDto;
import app.model.dto.*;
import app.services.CompanyService;
import app.services.ContractService;
import app.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
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

        for (TransactionItem transactionItem : contract.getTransactionItems()){
            BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), -1 * transactionItem.getAmount());

            RestTemplate rest = new RestTemplate();
            HttpEntity<BalanceUpdateDto> requestEntity = new HttpEntity<>(balanceUpdateDto, new HttpHeaders());
            ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/balance/reserve", HttpMethod.POST, requestEntity, String.class);
            try {
                String responseStr = Objects.requireNonNull(response.getBody());
                System.out.println(responseStr);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        contractService.finalizeContract(contract);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{id}/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createTransaction(@NotNull @PathVariable Long id, @RequestBody TransactionItemDTO transactionItemDTO) {
        Optional<Contract> optionalContract = contractService.findByID(id);

        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();

        if (contract.getStatus().equals(Status.FINALIZED))
            return ResponseEntity.badRequest().build();

        // todo sta ako je prodaja
        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(transactionItemDTO.getAccountId(), transactionItemDTO.getSecurityId(), transactionItemDTO.getSecurityType(), transactionItemDTO.getAmount());

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<BalanceUpdateDto> requestEntity = new HttpEntity<>(balanceUpdateDto, headers);
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/balance/reserve", HttpMethod.POST, requestEntity, String.class);
        String responseStr;
        try {
            responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        TransactionItem transactionItem = new TransactionItem(transactionItemDTO);
        contractService.createTransaction(contract, transactionItem);
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

        Optional<TransactionItem> optionalTransaction = transactionService.findByID(transactionID);

        if(optionalTransaction.isEmpty())
            return ResponseEntity.badRequest().build();

        TransactionItem transactionItem = optionalTransaction.get();

        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), -1 * transactionItem.getAmount());

        RestTemplate rest = new RestTemplate();
        HttpEntity<BalanceUpdateDto> requestEntity = new HttpEntity<>(balanceUpdateDto, new HttpHeaders());
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/balance/reserve", HttpMethod.POST, requestEntity, String.class);
        try {
            String responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        contractService.deleteTransaction(contract, transactionID);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/{contractID}/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTransaction(@NotNull @PathVariable Long contractID, @RequestBody TransactionItemDTO transactionItemDTO) {
        Optional<Contract> optionalContract = contractService.findByID(contractID);
        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();
        if (contract.getStatus().equals(Status.FINALIZED))
            return ResponseEntity.badRequest().build();

        Optional<TransactionItem> optionalTransaction = transactionService.findByID(transactionItemDTO.getId());
        if(optionalTransaction.isEmpty())
            return ResponseEntity.badRequest().build();

        TransactionItem transactionItem = optionalTransaction.get();

        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), -1 * transactionItem.getAmount());

        RestTemplate rest = new RestTemplate();
        HttpEntity<BalanceUpdateDto> requestEntity = new HttpEntity<>(balanceUpdateDto, new HttpHeaders());
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/balance/reserve", HttpMethod.POST, requestEntity, String.class);
        try {
            String responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        balanceUpdateDto = new BalanceUpdateDto(transactionItemDTO.getAccountId(), transactionItemDTO.getSecurityId(), transactionItemDTO.getSecurityType(),  transactionItemDTO.getAmount());
        rest = new RestTemplate();
        requestEntity = new HttpEntity<>(balanceUpdateDto, new HttpHeaders());
        response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/balance/reserve", HttpMethod.POST, requestEntity, String.class);
        try {
            String responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        contractService.updateTransaction(contract, transactionItemDTO);
        return ResponseEntity.ok().build();
    }

}
