package raf.osiguranje.accounttransaction.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.Transaction;
import raf.osiguranje.accounttransaction.model.dto.TransactionDTO;
import raf.osiguranje.accounttransaction.repositories.TransactionRepository;

import java.util.List;

@NoArgsConstructor
@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private BalanceService balanceService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, BalanceService balanceService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.balanceService = balanceService;
    }

    public Transaction getTransactionFromDto(TransactionDTO transactionDTO){
        return new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderId(),transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(), transactionDTO.getUsedReserve());
    }


    public boolean createTransaction(TransactionDTO transactionDTO) throws Exception{

        Account tmpAccount = accountService.findAccountById(transactionDTO.getAccountId());
        if(tmpAccount==null){
            throw new Exception("Couldn't find account");
        }

        Transaction transaction = getTransactionFromDto(transactionDTO);

        transactionRepository.save(transaction);

        return true;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(Long input){
        return transactionRepository.findAllByAccountId(input);
    }

    public List<Transaction> getTransactionsByOrderId(Long input){
        return transactionRepository.findAllByOrderId(input);
    }

    public List<Transaction> getTransactionsByUser(Long input){
        return transactionRepository.findAllByUserId(input);
    }

    public List<Transaction> getTransactionsByCurrency(Long input){
        return transactionRepository.findAllByCurrencyId(input);
    }


}
