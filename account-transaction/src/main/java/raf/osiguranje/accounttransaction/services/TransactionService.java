package raf.osiguranje.accounttransaction.services;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.Transaction;
import raf.osiguranje.accounttransaction.model.dto.*;
import raf.osiguranje.accounttransaction.repositories.TransactionRepository;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private BalanceService balanceService;
    private RestTemplate rest;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${api.buyingmarket}")
    private String buyingApiUrl;


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, BalanceService balanceService, RestTemplate rest) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.balanceService = balanceService;
        this.rest = rest;
    }



    public Transaction getTransactionFromDto(TransactionDTO transactionDTO){
        return new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(), transactionDTO.getUsedReserve(),transactionDTO.getText(),transactionDTO.getTransactionType());
    }
    public Transaction getTransactionFromOtcDto(TransactionOtcDto transactionDTO){
        return new Transaction(transactionDTO.getAccountId(),-1L,transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(), transactionDTO.getUsedReserve(),transactionDTO.getText(),transactionDTO.getTransactionType());
    }

    public Transaction createTransactionOtc(TransactionOtcDto transactionOtcDto, String jwt) throws Exception {
        Account tmpAccount = accountService.findAccountById(transactionOtcDto.getAccountId());
        if(tmpAccount==null){
            throw new Exception("Couldn't find account");
        }

        Transaction transaction = getTransactionFromOtcDto(transactionOtcDto);

        Optional<Balance> balanceCurrencyOptional = balanceService.getBalancesByFullId(transactionOtcDto.getAccountId(),transactionOtcDto.getCurrencyId(),SecurityType.CURRENCY);
        Optional<Balance> balanceSecurityOptional = balanceService.getBalancesByFullId(transactionOtcDto.getAccountId(),transactionOtcDto.getSecurityId(),transactionOtcDto.getSecurityType());
        if(balanceCurrencyOptional.isEmpty())
            throw new Exception("Couldn't find currency balaces");

        Balance balanceCurrency = balanceCurrencyOptional.get();
        Balance balanceSecurity = balanceSecurityOptional.orElse(balanceService.createBalance(transaction.getAccountId(),transactionOtcDto.getSecurityId(),transactionOtcDto.getSecurityType(),0,jwt));

        if(transaction.getTransactionType().equals(TransactionType.BUY)){
            if(transaction.getReserve() != 0){
                int val = transaction.getReserve();
                balanceService.updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,val),jwt);
            }
            if(transaction.getUsedReserve() != 0){
                int val = -transaction.getUsedReserve();
                balanceService.updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,val),jwt);
            }
            if(transaction.getPayout() != 0){
                int val = transaction.getPayout();
                balanceService.updateAmount(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(),val),jwt);
            }
            if(transaction.getPayment() != 0){
                int val = transaction.getPayment();
                balanceService.updateAmount(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(),val),jwt);
            }
        }else{
            if(transaction.getReserve() != 0){
                int val = transaction.getReserve();
                balanceService.updateReserve(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(),val),jwt);
            }
            if(transaction.getUsedReserve() != 0){
                int val = -transaction.getUsedReserve();
                balanceService.updateReserve(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(),val),jwt);
            }
            if(transaction.getPayout() != 0){
                int val = transaction.getPayout();
                balanceService.updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,val),jwt);
            }
            if(transaction.getPayment() != 0){
                int val = transaction.getPayment();
                balanceService.updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,val),jwt);
            }
        }

        transactionRepository.save(transaction);

        return transaction;
    }


    public Transaction createTransaction(TransactionDTO transactionDTO, String jwt) throws Exception{
        System.out.println(transactionDTO);
        Account tmpAccount = accountService.findAccountById(transactionDTO.getAccountId());
        if(tmpAccount==null){
            throw new Exception("Couldn't find account");
        }

        OrderDto orderDto = transactionDTO.getOrderDto();


        Transaction transaction = getTransactionFromDto(transactionDTO);


        Optional<Balance> balanceCurrencyOptional = balanceService.getBalancesByFullId(transaction.getAccountId(),transactionDTO.getCurrencyId(),SecurityType.CURRENCY);
        Optional<Balance> balanceSecurityOptional = balanceService.getBalancesByFullId(transaction.getAccountId(),orderDto.getSecurityId(),orderDto.getSecurityType());
        if(balanceCurrencyOptional.isEmpty())
            throw new Exception("Couldn't find currency balaces");

        Balance balanceCurrency = balanceCurrencyOptional.get();
        Balance balanceSecurity = balanceSecurityOptional.orElse(null);

        if(transactionDTO.getTransactionType().equals(TransactionType.BUY)){
            if(balanceSecurity == null){
                balanceSecurity = balanceService.createBalance(transaction.getAccountId(),orderDto.getSecurityId(),orderDto.getSecurityType(),0,jwt);
            }

            if(transaction.getUsedReserve() > 0){
                int value = -transaction.getUsedReserve();
                balanceService.updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,value),jwt);
                balanceService.updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,value),jwt);

                balanceService.updateAmount(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(),transaction.getPayment()),jwt);
            } else if(transaction.getPayment() > 0){
                balanceService.updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,transaction.getPayment()),jwt);
            } else if(transaction.getPayout() > 0){
                int value = -transaction.getPayout();
                balanceService.updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,value),jwt);
            } else if(transaction.getReserve() > 0){
                int value = transaction.getReserve();
                balanceService.updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,value),jwt);
                if(balanceSecurity==null){
                    balanceService.createBalance(transaction.getAccountId(),orderDto.getSecurityId(),orderDto.getSecurityType(),0,jwt);
                }
            }
        } else {

            if(transaction.getUsedReserve() > 0){
                if(balanceSecurity == null){
                    throw new Exception("Trying to sell something you dont have!!! " + orderDto.getSecurityId() + "  "+orderDto.getSecurityType());
                }
                int value = -transaction.getUsedReserve();
                balanceService.updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY,transaction.getPayment()),jwt);

                balanceService.updateReserve(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(),value),jwt);
                balanceService.updateAmount(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(),value),jwt);
            } else if(transaction.getPayment() > 0){
                if(balanceSecurity==null){
                    balanceService.createBalance(transaction.getAccountId(),orderDto.getSecurityId(),orderDto.getSecurityType(),0,jwt);
                }
                balanceService.updateAmount(new BalanceUpdateDto(transaction.getAccountId(),orderDto.getSecurityId(),orderDto.getSecurityType(),transaction.getPayment()),jwt);
            } else if(transaction.getPayout() > 0){
                int value = -transaction.getPayout();
                balanceService.updateAmount(new BalanceUpdateDto(transaction.getAccountId(),orderDto.getSecurityId(),orderDto.getSecurityType(),value),jwt);
            } else if(transaction.getReserve() > 0){
                int value = transaction.getReserve();
                balanceService.updateReserve(new BalanceUpdateDto(transaction.getAccountId(),orderDto.getSecurityId(),orderDto.getSecurityType(),value),jwt);

            }
        }

        transactionRepository.save(transaction);

        return transaction;
    }

    public void updateBalanceTransaction(Long accountId, Long securityId, SecurityType securityType,int amount,String jwt) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setOrderId(-1L);
        transaction.setText("Update balance amount: "+amount);
        transaction.setPayment(amount);
        balanceService.updateAmount(new BalanceUpdateDto(accountId,securityId,securityType,amount),jwt);
        transactionRepository.save(transaction);
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
