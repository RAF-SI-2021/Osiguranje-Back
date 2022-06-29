package raf.osiguranje.accounttransaction.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.dto.BalanceDTO;
import raf.osiguranje.accounttransaction.model.dto.BalanceUpdateDto;
import raf.osiguranje.accounttransaction.services.BalanceService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/balance")
public class BalanceRestController {


    private final BalanceService balanceService;

    @Autowired
    public BalanceRestController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(path="/all",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BalanceDTO>> getAllBalance() {
        return ResponseEntity.ok(balanceService.getAllBalances().stream().map(Balance::getDto).collect(Collectors.toList()));
    }

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BalanceDTO>> getBalance(@RequestParam("account") Optional<Long> accountId, @RequestParam("security") Optional<Long> securityId) {

        if (accountId.isPresent()) {
            if (securityId.isPresent()) {
                Optional<Balance> balance = balanceService.getBalancesByFullId(accountId.get(), securityId.get());
                return balance.map(value -> ResponseEntity.ok(Collections.singletonList(value.getDto()))).orElseGet(() -> ResponseEntity.badRequest().build());
            } else {
                List<Balance> balance = balanceService.getBalancesByAccount(accountId.get());

                return ResponseEntity.ok(balance.stream().map(Balance::getDto).collect(Collectors.toList()));
            }
        } else {
            if (securityId.isPresent()) {
                List<Balance> balance = balanceService.getBalancesBySecurity(securityId.get());
                return ResponseEntity.ok(balance.stream().map(Balance::getDto).collect(Collectors.toList()));
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveBalance(@RequestBody BalanceDTO input) {
        System.out.println(input.toString());
        if (balanceService.createBalance(input.getAccountId(), input.getSecurityId(), input.getSecurityType(),input.getAmount()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBalance(@RequestBody BalanceDTO input){
        System.out.println(input);
        if(balanceService.deleteBalance(input.getAccountId(), input.getSecurityId()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @PostMapping(path="/amount",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAmount(@RequestBody BalanceUpdateDto input){

        if(balanceService.updateAmount(input.getAccountId(), input.getSecurityId(), input.getAmount())){
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path="/reserve",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReserved(@RequestBody BalanceUpdateDto input){

        if(balanceService.updateReserve(input.getAccountId(), input.getSecurityId(), input.getAmount())){
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
