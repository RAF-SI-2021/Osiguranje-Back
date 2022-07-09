package app.controller;

import app.Config;
import app.model.*;
import app.model.api.TransactionOtcDto;
import app.model.dto.*;
import app.services.CompanyService;
import app.services.ContractService;
import app.services.TransactionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;
    private final CompanyService companyService;
    private final TransactionItemService transactionItemService;

    @Autowired
    public ContractController(ContractService contractService, CompanyService companyService, TransactionItemService transactionItemService) {
        this.contractService = contractService;
        this.companyService = companyService;
        this.transactionItemService = transactionItemService;
    }

    // todo da li contract moze da se obrise

    @CrossOrigin(origins = "*")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getContracts(@RequestHeader("Authorization") String authorization){
        List<ContractDTO> data = ListMapper.contractToContractDTO(contractService.findAll());
        return ResponseEntity.ok(data);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getContract(@NotNull @PathVariable Long id, @RequestHeader("Authorization") String authorization){
        Optional<Contract> optionalContract = contractService.findByID(id);
        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();

        Contract contract = optionalContract.get();
        return ResponseEntity.ok(new ContractDTO(contract));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createContract(@RequestBody CreateContractDTO createContractDTO, @RequestHeader("Authorization") String authorization) {
        Optional<Company> optionalCompany = companyService.findByID(createContractDTO.getCompanyID());
        if(optionalCompany.isEmpty())
            return ResponseEntity.badRequest().build();
        Company company = optionalCompany.get();
        Contract contract = new Contract(createContractDTO);

        contract.setCompany(company);
        contractService.save(contract);

        company.getContracts().add(contract);
        companyService.save(company);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/{id}/finalize", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> finalizeContract(@NotNull @PathVariable Long id, @RequestHeader("Authorization") String jwt) {
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
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization","Bearer " + jwt);
            HttpEntity<TransactionOtcDto> requestEntity = new HttpEntity<>(transactionOtcDto, headers);
            ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transaction/otc", HttpMethod.POST, requestEntity, String.class);
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
    @PostMapping(path = "/{id}/transaction-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createTransactionItem(@NotNull @PathVariable Long id, @RequestBody TransactionItemDTO transactionItemDTO, @RequestHeader("Authorization") String authorization) {
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
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer " + authorization);

        HttpEntity<?> requestEntity = new HttpEntity<>(transactionOtcDto, headers);

        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transaction/otc", HttpMethod.POST, requestEntity, String.class);
        String responseStr;
        try {
            responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        TransactionItem transactionItem = new TransactionItem(transactionItemDTO);
        contractService.createTransactionItem(contract, transactionItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/{contractID}/transaction-items/{transactionID}")
    public ResponseEntity<?> deleteTransactionItem(@NotNull @PathVariable Long contractID, @NotNull @PathVariable Long transactionID, @RequestHeader("Authorization") String authorization) {
        Optional<Contract> optionalContract = contractService.findByID(contractID);

        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();

        if (contract.getStatus().equals(Status.FINALIZED))
            return ResponseEntity.badRequest().build();

        Optional<TransactionItem> optionalTransactionItem = transactionItemService.findByID(transactionID);

        if(optionalTransactionItem.isEmpty())
            return ResponseEntity.badRequest().build();

        TransactionItem transactionItem = optionalTransactionItem.get();

        int usedReserve;
        if(transactionItem.getTransactionType().equals(TransactionType.BUY))
            usedReserve = (int) (transactionItem.getAmount() * transactionItem.getPricePerShare());
        else
            usedReserve = transactionItem.getAmount();

        TransactionOtcDto transactionOtcDto = new TransactionOtcDto(transactionItem.getTransactionType(), transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), 0L, transactionItem.getCurrencyId(), 0, 0, 0, usedReserve);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + authorization);
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate rest = new RestTemplate();
        HttpEntity<TransactionOtcDto> requestEntity = new HttpEntity<>(transactionOtcDto, headers);
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transaction/otc", HttpMethod.POST, requestEntity, String.class);
        try {
            Objects.requireNonNull(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        contractService.deleteTransactionItem(contract, transactionID);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/{contractID}/transaction-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTransactionItem(@NotNull @PathVariable Long contractID, @RequestBody TransactionItemDTO transactionItemDTO, @RequestHeader("Authorization") String authorization) {
        Optional<Contract> optionalContract = contractService.findByID(contractID);
        if(optionalContract.isEmpty())
            return ResponseEntity.badRequest().build();
        Contract contract = optionalContract.get();
        if (contract.getStatus().equals(Status.FINALIZED))
            return ResponseEntity.badRequest().build();

        Optional<TransactionItem> optionalTransactionItem = transactionItemService.findByID(transactionItemDTO.getId());
        if(optionalTransactionItem.isEmpty())
            return ResponseEntity.badRequest().build();

        TransactionItem transactionItem = optionalTransactionItem.get();

        int reserve;
        if(transactionItem.getTransactionType().equals(TransactionType.BUY))
            reserve = (int) (- 1 * transactionItem.getAmount() * transactionItem.getPricePerShare());
        else
            reserve = -1 * transactionItem.getAmount();

        TransactionOtcDto transactionOtcDto = new TransactionOtcDto(transactionItem.getTransactionType(), transactionItem.getAccountId(), transactionItem.getSecurityId(), transactionItem.getSecurityType(), 0L, transactionItem.getCurrencyId(), 0, 0, reserve, 0);

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + authorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionOtcDto> requestEntity = new HttpEntity<>(transactionOtcDto, headers);
        ResponseEntity<String> response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transaction/otc", HttpMethod.POST, requestEntity, String.class);
        String responseStr;
        try {
            responseStr = Objects.requireNonNull(response.getBody());
            System.out.println(responseStr);
        } catch (Exception e) {}

        if(transactionItemDTO.getTransactionType().equals(TransactionType.BUY))
            reserve = (int) (transactionItemDTO.getAmount() * transactionItemDTO.getPricePerShare());
        else
            reserve = transactionItemDTO.getAmount();

        transactionOtcDto = new TransactionOtcDto(transactionItemDTO.getTransactionType(), transactionItemDTO.getAccountId(), transactionItemDTO.getSecurityId(), transactionItemDTO.getSecurityType(), 0L, transactionItemDTO.getCurrencyId(), 0, 0, reserve, 0);

        rest = new RestTemplate();
        headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + authorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        requestEntity = new HttpEntity<>(transactionOtcDto, headers);
        response = rest.exchange(Config.getProperty("accounts_api_url") + "/api/transaction/otc", HttpMethod.POST, requestEntity, String.class);
        try {
            Objects.requireNonNull(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        contractService.updateTransactionItem(contract, transactionItemDTO);
        return ResponseEntity.ok().build();
    }

}
