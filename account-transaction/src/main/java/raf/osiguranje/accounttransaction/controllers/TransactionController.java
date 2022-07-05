package raf.osiguranje.accounttransaction.controllers;

import accounts.TransactionDto;
import accounts.TransactionOtcDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.osiguranje.accounttransaction.model.Transaction;
import raf.osiguranje.accounttransaction.services.TransactionService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDto input,@RequestHeader("Authorization") String authorization){

        try {
            Transaction tr = transactionService.createTransaction(input,authorization);
            return ResponseEntity.accepted().body(tr.getDto());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/otc")
    public ResponseEntity<?> createTransactionOtc(@RequestBody TransactionOtcDto input, @RequestHeader("Authorization") String authorization){

        try {
            transactionService.createTransactionOtc(input,authorization);
            return ResponseEntity.accepted().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping(path="/all")
    public ResponseEntity<List<TransactionDto>> findAllTransactions(@RequestHeader("Authorization") String authorization){
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions.stream().map(Transaction::getDto).collect(Collectors.toList()));
    }

    @GetMapping(path="/account")
    public ResponseEntity<List<TransactionDto>> findAllTransactionsByAccount(@RequestParam("account") Long input,@RequestHeader("Authorization") String authorization){
        List<Transaction> transactions = transactionService.getTransactionsByAccount(input);
        return ResponseEntity.ok(transactions.stream().map(Transaction::getDto).collect(Collectors.toList()));
    }

    @GetMapping(path="/currency")
    public ResponseEntity<List<TransactionDto>> findAllTransactionsByForex(@RequestParam("currency") Long input,@RequestHeader("Authorization") String authorization){
        List<Transaction> transactions = transactionService.getTransactionsByCurrency(input);
        return ResponseEntity.ok(transactions.stream().map(Transaction::getDto).collect(Collectors.toList()));
    }

    @GetMapping(path="/user")
    public ResponseEntity<List<TransactionDto>> findAllTransactionsByUser(@RequestParam("user") Long input,@RequestHeader("Authorization") String authorization){
        List<Transaction> transactions = transactionService.getTransactionsByUser(input);
        return ResponseEntity.ok(transactions.stream().map(Transaction::getDto).collect(Collectors.toList()));
    }

    @GetMapping(path="/order")
    public ResponseEntity<List<TransactionDto>> findAllTransactionsByOrder(@RequestParam("order") Long input,@RequestHeader("Authorization") String authorization){
        List<Transaction> transactions = transactionService.getTransactionsByOrderId(input);
        return ResponseEntity.ok(transactions.stream().map(Transaction::getDto).collect(Collectors.toList()));
    }

}
