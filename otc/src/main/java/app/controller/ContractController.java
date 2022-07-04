package app.controller;

import app.Config;
import app.model.*;
import app.model.api.TransactionOtcDto;
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
            int usedReserve;
            int payment;
            int payout;
            if(transactionItem.getTransactionType().equals(TransactionType.BUY)){
                usedReserve = (int) (transactionItem.getAmount() * transactionItem.getPricePerShare());
                payment = usedReserve;
                payout = transactionItem.getAmount();
            }
            else {
                usedReserve = transactionItem.getAmount();
                payment = usedReserve;
                payout = (int) (transactionItem.getAmount() * transactionItem.getPricePerShare());
            }

            TransactionOtcDto transactionOtcDto = new TransactionOtcDto(transactionItem.getTransactionType(), transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), 0L, transactionItem.getCurrencyId(), payment, payout, 0, usedReserve);

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<TransactionOtcDto> requestEntity = new HttpEntity<>(transactionOtcDto, headers);
            ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transactions/otc", HttpMethod.POST, requestEntity, String.class);
            String responseStr;
            try {
                responseStr = Objects.requireNonNull(response.getBody());
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

        int reserve;
        if(transactionItemDTO.getTransactionType().equals(TransactionType.BUY))
            reserve = (int) (transactionItemDTO.getAmount() * transactionItemDTO.getPricePerShare());
        else
            reserve = transactionItemDTO.getAmount();

        TransactionOtcDto transactionOtcDto = new TransactionOtcDto(transactionItemDTO.getTransactionType(), transactionItemDTO.getAccountId(), transactionItemDTO.getSecurityId(), transactionItemDTO.getSecurityType(), 0L, transactionItemDTO.getCurrencyId(), 0, 0, reserve, 0);

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TransactionOtcDto> requestEntity = new HttpEntity<>(transactionOtcDto, headers);
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transactions/otc", HttpMethod.POST, requestEntity, String.class);
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

        int usedReserve;
        if(transactionItem.getTransactionType().equals(TransactionType.BUY))
            usedReserve = (int) (transactionItem.getAmount() * transactionItem.getPricePerShare());
        else
            usedReserve = transactionItem.getAmount();

        TransactionOtcDto transactionOtcDto = new TransactionOtcDto(transactionItem.getTransactionType(), transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), 0L, transactionItem.getCurrencyId(), 0, 0, 0, usedReserve);

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TransactionOtcDto> requestEntity = new HttpEntity<>(transactionOtcDto, headers);
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transactions/otc", HttpMethod.POST, requestEntity, String.class);
        String responseStr;
        try {
            responseStr = Objects.requireNonNull(response.getBody());
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

        int reserve;
        if(transactionItem.getTransactionType().equals(TransactionType.BUY))
            reserve = (int) (- 1 * transactionItem.getAmount() * transactionItem.getPricePerShare());
        else
            reserve = -1 * transactionItem.getAmount();

        TransactionOtcDto transactionOtcDto = new TransactionOtcDto(transactionItem.getTransactionType(), transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), 0L, transactionItem.getCurrencyId(), 0, 0, reserve, 0);

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TransactionOtcDto> requestEntity = new HttpEntity<>(transactionOtcDto, headers);
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transactions/otc", HttpMethod.POST, requestEntity, String.class);
        String responseStr;
        try {
            responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(transactionItemDTO.getTransactionType().equals(TransactionType.BUY))
            reserve = (int) (transactionItemDTO.getAmount() * transactionItemDTO.getPricePerShare());
        else
            reserve = transactionItemDTO.getAmount();

        transactionOtcDto = new TransactionOtcDto(transactionItemDTO.getTransactionType(), transactionItemDTO.getAccountId(), transactionItemDTO.getSecurityId(), transactionItemDTO.getSecurityType(), 0L, transactionItemDTO.getCurrencyId(), 0, 0, reserve, 0);

        rest = new RestTemplate();
        headers = new HttpHeaders();
        requestEntity = new HttpEntity<>(transactionOtcDto, headers);
        response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transactions/otc", HttpMethod.POST, requestEntity, String.class);
        try {
            responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        contractService.updateTransaction(contract, transactionItemDTO);
        return ResponseEntity.ok().build();
    }

}
