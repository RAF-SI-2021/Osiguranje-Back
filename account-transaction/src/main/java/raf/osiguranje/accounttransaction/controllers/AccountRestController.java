package raf.osiguranje.accounttransaction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;
import raf.osiguranje.accounttransaction.services.AccountService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/account")
public class AccountRestController {

    private AccountService accountService;

    @Autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> findAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAccount(@RequestParam("id")Optional<Long> id, @RequestParam("type")Optional<String> typeInput){
        String typeString = typeInput.orElse("");

        if(id.isPresent()) {
            Account account = accountService.findAccountById(id.get());
            if(account==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return ResponseEntity.ok(Collections.singletonList(account));
            }
        } else if (!typeString.isEmpty()){
            AccountType accountType;
            try {
                 accountType = AccountType.valueOf(typeString);

            }catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }

            List<Account> accounts = accountService.findAccountByAccountType(accountType);

            return ResponseEntity.ok(accounts);
        } else {
            return ResponseEntity.ok(accountService.getAllAccounts());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestParam("type") Optional<String> typeInput){
        String typeString = typeInput.orElse("");
        if (typeString.isEmpty()) {
            Account account = accountService.createNewAccount();
            return ResponseEntity.ok(account);
        } else {
            AccountType accountType;
            try {
                accountType = AccountType.valueOf(typeString);
            }catch (IllegalArgumentException e){
                return ResponseEntity.badRequest().build();
            }

            Account account = accountService.createNewAccount(accountType);
            return ResponseEntity.ok(account);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@RequestParam("id") Long idInput) {
        Account account = accountService.findAccountById(idInput);
        if (account == null) {
            return ResponseEntity.badRequest().build();
        }
        accountService.deleteAccount(account);
        return ResponseEntity.ok().build();
    }
}
