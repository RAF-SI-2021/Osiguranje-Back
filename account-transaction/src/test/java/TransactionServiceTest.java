import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.Transaction;
import raf.osiguranje.accounttransaction.model.dto.*;
import raf.osiguranje.accounttransaction.repositories.TransactionRepository;
import raf.osiguranje.accounttransaction.services.AccountService;
import raf.osiguranje.accounttransaction.services.BalanceService;
import raf.osiguranje.accounttransaction.services.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    private TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private AccountService accountService = mock(AccountService.class);
    private BalanceService balanceService = mock(BalanceService.class);
    private RestTemplate rest = mock(RestTemplate.class);

    private TransactionService underTest;

    private static final Long accountId = 1L;
    private static final Long securityId = 1L;
    private static final String jwt = "notomorrow";

    @BeforeEach
    void setUp() {
        underTest = new TransactionService(transactionRepository, accountService, balanceService, rest);
    }

    @Test
    public void getTransactionFromDto() {
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                                        BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                                                    1L, "TEXT", 1, 1, 1, 1,
                                                            TransactionType.BUY);

        Transaction actual = underTest.getTransactionFromDto(transactionDTO);

        assertEquals(transactionDTO.getAccountId(), actual.getAccountId());
        assertEquals(transactionDTO.getOrderDto().getOrderId(), actual.getOrderId());
        assertEquals(transactionDTO.getUserId(), actual.getUserId());
        assertEquals(transactionDTO.getCurrencyId(), actual.getCurrencyId());
        assertEquals(transactionDTO.getPayment(), actual.getPayment());
        assertEquals(transactionDTO.getPayout(), actual.getPayout());
        assertEquals(transactionDTO.getReserve(), actual.getReserve());
        assertEquals(transactionDTO.getUsedReserve(), actual.getUsedReserve());
        assertEquals(transactionDTO.getText(), actual.getText());
        assertEquals(transactionDTO.getTransactionType(), actual.getTransactionType());
    }

    @Test
    public void getTransactionFromOtcDto() {
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                                                                    1L, 1L, "TEXT", 1, 1,
                                                                    1, 1, TransactionType.BUY);

        Transaction actual = underTest.getTransactionFromOtcDto(transactionOtcDTO);

        assertEquals(transactionOtcDTO.getAccountId(), actual.getAccountId());
        assertEquals(-1L, actual.getOrderId());
        assertEquals(transactionOtcDTO.getUserId(), actual.getUserId());
        assertEquals(transactionOtcDTO.getCurrencyId(), actual.getCurrencyId());
        assertEquals(transactionOtcDTO.getPayment(), actual.getPayment());
        assertEquals(transactionOtcDTO.getPayout(), actual.getPayout());
        assertEquals(transactionOtcDTO.getReserve(), actual.getReserve());
        assertEquals(transactionOtcDTO.getUsedReserve(), actual.getUsedReserve());
        assertEquals(transactionOtcDTO.getText(), actual.getText());
        assertEquals(transactionOtcDTO.getTransactionType(), actual.getTransactionType());
    }

    @Test
    public void createTransactionOtc_ThrowsException_IfAccountNull() {
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 1, 1,
                1, 1, TransactionType.BUY);

        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            underTest.createTransactionOtc(transactionOtcDTO, jwt);
        });

        String expectedMessage = "Couldn't find account";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createTransactionOtc_ThrowsException_IfBalanceEmpty() {
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 1, 1,
                1, 1, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                                                SecurityType.CURRENCY)).thenReturn(Optional.empty());
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                                                transactionOtcDTO.getSecurityType())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            underTest.createTransactionOtc(transactionOtcDTO, jwt);
        });

        String expectedMessage = "Couldn't find currency balaces";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createTransactionOtc_TransactionTypeBuy1() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 0, 0,
                1, 0, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, transaction.getReserve()),jwt);
    }

    @Test
    public void createTransactionOtc_TransactionTypeBuy2() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 0, 0,
                0, 1, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, -transaction.getUsedReserve()),jwt);
    }

    @Test
    public void createTransactionOtc_TransactionTypeBuy3() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 0, 1,
                0, 0, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),balanceSecurity.getSecurityType(), transaction.getPayout()),jwt);
    }

    @Test
    public void createTransactionOtc_TransactionTypeBuy4() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 1, 0,
                0, 0, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),balanceSecurity.getSecurityType(), transaction.getPayment()),jwt);
    }

    @Test
    public void createTransactionOtc_TransactionTypeNOTBuy1() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 0, 0,
                1, 0, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),balanceSecurity.getSecurityType(), transaction.getReserve()),jwt);
    }

    @Test
    public void createTransactionOtc_TransactionTypeNOTBuy2() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 0, 0,
                0, 1, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),balanceSecurity.getSecurityType(), -transaction.getUsedReserve()),jwt);
    }

    @Test
    public void createTransactionOtc_TransactionTypeNOTBuy3() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 0, 1,
                0, 0, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, transaction.getPayout()),jwt);
    }

    @Test
    public void createTransactionOtc_TransactionTypeNOTBuy4() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        TransactionOtcDto transactionOtcDTO = new TransactionOtcDto(1L, accountId, securityId, SecurityType.FOREX,
                1L, 1L, "TEXT", 1, 0,
                0, 0, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);
        Transaction transaction = new Transaction(transactionOtcDTO.getAccountId(),-1L,transactionOtcDTO.getUserId(),transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getPayment(),transactionOtcDTO.getPayout(),transactionOtcDTO.getReserve(), transactionOtcDTO.getUsedReserve(),transactionOtcDTO.getText(),transactionOtcDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromOtcDto(transactionOtcDTO);
        when(accountService.findAccountById(transactionOtcDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionOtcDTO.getAccountId(), transactionOtcDTO.getCurrencyId(),
                transactionOtcDTO.getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransactionOtc(transactionOtcDTO, jwt);

        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, transaction.getPayment()),jwt);
    }

    @Test
    public void createTransaction_ThrowsException_IfAccountNull() {
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                                                            1L, "TEXT", 1, 1, 1,
                                                            1, TransactionType.BUY);

        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            underTest.createTransaction(transactionDTO, jwt);
        });

        String expectedMessage = "Couldn't find account";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createTransaction_ThrowsException_IfBalanceEmpty() {
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 1, 1, 1,
                1, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.empty());
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            underTest.createTransaction(transactionDTO, jwt);
        });

        String expectedMessage = "Couldn't find currency balaces";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createTransaction_TransactionTypeBuy1() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 0, 0, 0,
                1, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = -transaction.getUsedReserve();
        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, value),jwt);
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, value),jwt);
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(), transaction.getPayment()),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void createTransaction_TransactionTypeBuy2() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 1, 0, 0,
                0, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = transaction.getPayment();
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, value),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void createTransaction_TransactionTypeBuy3() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 0, 1, 0,
                0, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = -transaction.getPayout();
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, value),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void createTransaction_TransactionTypeBuy4() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 0, 0, 1,
                0, TransactionType.BUY);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = transaction.getReserve();
        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceCurrency.getSecurityId(),SecurityType.CURRENCY, value),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void createTransaction_TransactionTypeNOTBuy1() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 0, 0, 0,
                1, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = -transaction.getUsedReserve();
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceCurrency.getAccountId(),balanceSecurity.getSecurityId(),SecurityType.CURRENCY, transaction.getPayment()),jwt);
        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(), value),jwt);
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(balanceSecurity.getAccountId(),balanceSecurity.getSecurityId(),balanceSecurity.getSecurityType(), value),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void createTransaction_TransactionTypeNOTBuy2() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 1, 0, 0,
                0, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = transaction.getPayment();
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(transaction.getAccountId(),transactionDTO.getOrderDto().getSecurityId(),transactionDTO.getOrderDto().getSecurityType(), value),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void createTransaction_TransactionTypeNOTBuy3() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 0, 1, 0,
                0, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = -transaction.getPayout();
        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(transaction.getAccountId(),transactionDTO.getOrderDto().getSecurityId(),transactionDTO.getOrderDto().getSecurityType(), value),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void createTransaction_TransactionTypeNOTBuy4() throws Exception {
        TransactionService transactionServiceSpy = spy(underTest);
        OrderDto orderDto = new OrderDto(1L, securityId, 1L, 1, SecurityType.FOREX,
                true, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, true);
        TransactionDTO transactionDTO = new TransactionDTO(1L, accountId, LocalDateTime.now(), orderDto, 1L,
                1L, "TEXT", 0, 0, 1,
                0, TransactionType.SELL);
        Account account = new Account();
        Balance balanceCurrency = new Balance(account, securityId, SecurityType.CURRENCY, 1);
        Balance balanceSecurity = new Balance(account, securityId, SecurityType.FOREX, 1);

        Transaction transaction = new Transaction(transactionDTO.getAccountId(),transactionDTO.getOrderDto().getOrderId(),
                transactionDTO.getUserId(),transactionDTO.getCurrencyId(),
                transactionDTO.getPayment(),transactionDTO.getPayout(),transactionDTO.getReserve(),
                transactionDTO.getUsedReserve(),transactionDTO.getText(),
                transactionDTO.getTransactionType());

        doReturn(transaction).when(transactionServiceSpy).getTransactionFromDto(transactionDTO);
        when(accountService.findAccountById(transactionDTO.getAccountId())).thenReturn(account);
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getCurrencyId(),
                SecurityType.CURRENCY)).thenReturn(Optional.of(balanceCurrency));
        when(balanceService.getBalancesByFullId(transactionDTO.getAccountId(), transactionDTO.getOrderDto().getSecurityId(),
                transactionDTO.getOrderDto().getSecurityType())).thenReturn(Optional.of(balanceSecurity));

        underTest.createTransaction(transactionDTO, jwt);

        int value = transaction.getReserve();
        verify(balanceService, times(1)).updateReserve(new BalanceUpdateDto(transaction.getAccountId(),transactionDTO.getOrderDto().getSecurityId(),transactionDTO.getOrderDto().getSecurityType(), value),jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void updateBalanceTransaction_CallsTransactionRepositorySave_AndBalanceServiceUpdateAmount_Once() throws Exception {
        SecurityType securityType = SecurityType.FOREX;
        int amount = 1;
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setOrderId(-1L);
        transaction.setText("Update balance amount: " + amount);
        transaction.setPayment(amount);

        underTest.updateBalanceTransaction(accountId, securityId, securityType, amount, jwt);

        verify(balanceService, times(1)).updateAmount(new BalanceUpdateDto(accountId, securityId, securityType, amount), jwt);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void getAllTransactions_CallsTransactionRepositoryFindAll_Once() {
        underTest.getAllTransactions();

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void getTransactionsByAccount_CallsTransactionRepositoryFindAllByAccountId_Once() {
        Long id = 1L;
        underTest.getTransactionsByAccount(id);

        verify(transactionRepository, times(1)).findAllByAccountId(id);
    }

    @Test
    public void getTransactionsByOrderId_CallsTransactionRepositoryFindAllByOrderId_Once() {
        Long id = 1L;
        underTest.getTransactionsByOrderId(id);

        verify(transactionRepository, times(1)).findAllByOrderId(id);
    }

    @Test
    public void getTransactionsByUser_CallsTransactionRepositoryFindAllByUserId_Once() {
        Long id = 1L;
        underTest.getTransactionsByUser(id);

        verify(transactionRepository, times(1)).findAllByUserId(id);
    }

    @Test
    public void getTransactionsByCurrency_CallsTransactionRepositoryFindAllByCurrencyId_Once() {
        Long id = 1L;
        underTest.getTransactionsByCurrency(id);

        verify(transactionRepository, times(1)).findAllByCurrencyId(id);
    }
}
