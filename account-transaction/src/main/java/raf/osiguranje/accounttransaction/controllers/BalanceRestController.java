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
    public ResponseEntity<List<BalanceDTO>> getAllBalance(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(balanceService.getAllBalances().stream().map(Balance::getDto).collect(Collectors.toList()));
    }

    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BalanceDTO>> getBalance(@RequestHeader("Authorization") String authorization,@RequestParam("account") Optional<Long> accountId, @RequestParam("security") Optional<Long> securityId) {

        if (accountId.isPresent()) {
            List<Balance> balance;
            if (securityId.isPresent()) {
                balance = balanceService.getBalancesByAccountAndSecurity(accountId.get(), securityId.get());
            } else {
                balance = balanceService.getBalancesByAccount(accountId.get());

            }
            return ResponseEntity.ok(balance.stream().map(Balance::getDto).collect(Collectors.toList()));
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
    public ResponseEntity<?> saveBalance(@RequestBody BalanceDTO input,@RequestHeader("Authorization") String authorization) {
        System.out.println(input.toString());

        try {
            balanceService.createBalance(input.getAccountId(), input.getSecurityId(), input.getSecurityType(),input.getAmount(),authorization);
            return ResponseEntity.accepted().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBalance(@RequestBody BalanceDTO input,@RequestHeader("Authorization") String authorization){
        System.out.println(input);
        try {
            balanceService.deleteBalance(input.getAccountId(), input.getSecurityId(),input.getSecurityType(),authorization);
            return ResponseEntity.accepted().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path="/amount",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAmount(@RequestBody BalanceUpdateDto input,@RequestHeader("Authorization") String authorization){

        try {
            balanceService.updateAmount(input,authorization);
            return ResponseEntity.accepted().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(path="/reserve",produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReserved(@RequestBody BalanceUpdateDto input,@RequestHeader("Authorization") String authorization){
        try {
            balanceService.updateReserve(input,authorization);
            return ResponseEntity.accepted().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
