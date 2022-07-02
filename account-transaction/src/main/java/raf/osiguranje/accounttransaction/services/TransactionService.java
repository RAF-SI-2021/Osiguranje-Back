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
        return new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderId(),transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(), transactionDTO.getUsedReserve(),transactionDTO.getTransactionType());
    }
    public Transaction getTransactionFromOtcDto(TransactionOtcDto transactionDTO){
        return new Transaction(transactionDTO.getAccountId(),-1L,transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(), transactionDTO.getUsedReserve(),transactionDTO.getTransactionType());
    }

    public boolean createTransactionOtc(TransactionOtcDto transactionOtcDto, String jwt) throws Exception {
        Account tmpAccount = accountService.findAccountById(transactionOtcDto.getAccountId());
        if(tmpAccount==null){
            throw new Exception("Couldn't find account");
        }

        Transaction transaction = getTransactionFromOtcDto(transactionOtcDto);

        Optional<Balance> balanceOptional = balanceService.getBalancesByFullId(transaction.getAccountId(),transactionOtcDto.getCurrencyId(),SecurityType.CURRENCY);


        if(transaction.getPayment() > 0){
            abstractPaymentChange(jwt, transaction, balanceOptional, transactionOtcDto.getCurrencyId());
        } else if(transaction.getPayout() > 0){
                balanceService.updateAmount(new BalanceUpdateDto(transaction.getAccountId(),transactionOtcDto.getCurrencyId(),SecurityType.CURRENCY,-transaction.getPayout()),jwt);
        }else {
            changeBalanceReserve(jwt, transaction, transactionOtcDto.getSecurityId(), transactionOtcDto.getSecurityType());
        }

        transactionRepository.save(transaction);

        return true;
    }


    public Transaction createTransaction(TransactionDTO transactionDTO, String jwt) throws Exception{
        System.out.println(transactionDTO);
        Account tmpAccount = accountService.findAccountById(transactionDTO.getAccountId());
        if(tmpAccount==null){
            throw new Exception("Couldn't find account");
        }

        Transaction transaction = getTransactionFromDto(transactionDTO);

        Optional<Balance> balanceOptional = balanceService.getBalancesByFullId(transaction.getAccountId(),transactionDTO.getCurrencyId(),SecurityType.CURRENCY);

        if(transactionDTO.getTransactionType().equals(TransactionType.BUY)){

        }

        if(transaction.getPayment() > 0){
            abstractPaymentChange(jwt, transaction, balanceOptional, transactionDTO.getCurrencyId());
        } else if(transaction.getPayout() > 0){
            balanceService.updateAmount(new BalanceUpdateDto(transaction.getAccountId(),transactionDTO.getCurrencyId(),SecurityType.CURRENCY,-transaction.getPayout()),jwt);
        }else {

            OrderDto orderDto = getOrderById(transactionDTO.getOrderId(), jwt);
            changeBalanceReserve(jwt, transaction, orderDto.getSecurityId(), orderDto.getSecurityType());
        }

        transactionRepository.save(transaction);

        return transaction;
    }


    private void abstractPaymentChange(String jwt, Transaction transaction, Optional<Balance> balanceOptional, Long currencyId ) throws Exception {
        if(balanceOptional.isEmpty()){
            try {
                balanceService.createBalance(transaction.getAccountId(), currencyId, SecurityType.CURRENCY, transaction.getPayment(), jwt);
            }catch (Exception e){
                throw new Exception("Wrong transaction. No balance. And error with balance create: "+e.getMessage());
            }
        } else {
            balanceService.updateAmount(new BalanceUpdateDto(transaction.getAccountId(), currencyId,SecurityType.CURRENCY,transaction.getPayment()),jwt);
        }
    }

    private void changeBalanceReserve(String jwt, Transaction transaction, Long securityId, SecurityType securityType) throws Exception {
        if (transaction.getReserve() > 0) {
            try {
                balanceService.updateReserve(new BalanceUpdateDto(transaction.getAccountId(), securityId, securityType, transaction.getReserve()), jwt);
            } catch (Exception e) {
                throw new Exception("Wrong transaction. And error with balance reservation: " + e.getMessage());
            }
        } else if (transaction.getUsedReserve() > 0) {
            int value = -transaction.getUsedReserve();
            try {
                balanceService.updateReserve(new BalanceUpdateDto(transaction.getAccountId(), securityId, securityType, value), jwt);
                balanceService.updateAmount(new BalanceUpdateDto(transaction.getAccountId(), securityId, securityType, value),jwt);
            } catch (Exception e) {
                throw new Exception("Wrong transaction. And error with balance reservation: " + e.getMessage());
            }
        }
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


    protected OrderDto getOrderById(Long orderId, String jwtToken) throws Exception {
        String urlString = buyingApiUrl + "/api/orders/"+orderId;
        ResponseEntity<OrderDto> response;
        try {
            response = rest.exchange(urlString, HttpMethod.GET, null, OrderDto.class);
        } catch(RestClientException e) {
            throw new Exception("Something went wrong while trying to retrieve security info");
        }
        OrderDto order = null;
        if(response.getBody() != null) {
            order = response.getBody();
        }
        if (order == null) {
            throw new IllegalArgumentException("Something went wrong trying to find buying market");
        }
        return order;
    }

}
