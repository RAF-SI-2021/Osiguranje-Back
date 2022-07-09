import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.Balance;
import raf.osiguranje.accounttransaction.model.BalanceId;
import raf.osiguranje.accounttransaction.model.dto.*;
import raf.osiguranje.accounttransaction.repositories.AccountRepository;
import raf.osiguranje.accounttransaction.repositories.BalanceRepository;
import raf.osiguranje.accounttransaction.services.BalanceService;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BalanceServiceTest {
    private AccountRepository accountRepository = mock(AccountRepository.class);
    private BalanceRepository balanceRepository = mock(BalanceRepository.class);
    private RestTemplate rest = mock(RestTemplate.class);

    private BalanceService underTest;

    private static final Long accountNumber = 1L;
    private static final Long securityId = 1L;
    private static final String jwt = "notomorrow";

    @BeforeEach
    void setUp() {
        underTest = new BalanceService(accountRepository, balanceRepository, rest);
    }

    @Test
    public void createBalance_ThrowsException_IfAccountNumber_Null() {
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = 1;
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            underTest.createBalance(null, securityId, securityType, amount, jwt);
        });

        String expectedMessage = "Couldn't find account";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createBalance_CallsBalanceRepositorySave_Once_ForSecurityTypeCurrency() throws Exception {
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = 1;
        Account account = new Account();
        BalanceService balanceServiceSpy = spy(underTest);
        UserDto userDto = new UserDto(1L, "FIRST_NAME", "LAST_NAME", "EMAIL",
                                "POSITION", "PHONE_NUMBER", true);
        CurrencyDTO currencyDTO = new CurrencyDTO(1L, "NAME", "ISO_CODE", "SYMBOL", "COUNTRY");
        Balance balance = new Balance(account, 1L, securityType, 1);

        doReturn(currencyDTO).when(balanceServiceSpy).getCurrencyById(securityId, jwt);
        doReturn(userDto).when(balanceServiceSpy).getUserByUsernameFromUserService("USERNAME");
        doReturn("USERNAME").when(balanceServiceSpy).extractUsername(jwt);
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(account);
        when(balanceRepository.findById(new BalanceId(accountNumber, securityId, securityType))).thenReturn(Optional.empty());

        Balance expected = new Balance(account, securityId, securityType, amount);
        Balance actual = balanceServiceSpy.createBalance(accountNumber, securityId, securityType, amount, jwt);

        verify(balanceRepository, times(1)).save(expected);
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountNumber);
        verify(balanceRepository, times(1)).findById(new BalanceId(accountNumber, securityId, securityType));
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getSecurityId(), actual.getSecurityId());
        assertEquals(expected.getSecurityType(), actual.getSecurityType());
        assertEquals(expected.getAmount(), actual.getAmount());
    }

    @Test
    public void createBalance_CallsBalanceRepositorySave_Once_ForSecurityTypeNOTCurrency() throws Exception {
        SecurityType securityType = SecurityType.FOREX;
        int amount = 1;
        Account account = new Account();
        BalanceService balanceServiceSpy = spy(underTest);
        UserDto userDto = new UserDto(1L, "FIRST_NAME", "LAST_NAME", "EMAIL",
                "POSITION", "PHONE_NUMBER", true);
        SecurityDTO securityDTO = new SecurityDTO(1, "TICKER", "NAME", "LAST_UPDATED",
                                                    BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                                                    BigDecimal.ONE, 1L, 1,
                                                    new HashSet<SecurityHistoryDTO>(), BigDecimal.ONE, BigDecimal.ONE,
                                                    BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
        Balance balance = new Balance(account, 1L, securityType, 1);

        doReturn(securityDTO).when(balanceServiceSpy).getSecurityByTypeAndId(securityType, securityId, jwt);
        doReturn(userDto).when(balanceServiceSpy).getUserByUsernameFromUserService("USERNAME");
        doReturn("USERNAME").when(balanceServiceSpy).extractUsername(jwt);
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(account);
        when(balanceRepository.findById(new BalanceId(accountNumber, securityId, securityType))).thenReturn(Optional.empty());

        Balance expected = new Balance(account, securityId, securityType, amount);
        Balance actual = balanceServiceSpy.createBalance(accountNumber, securityId, securityType, amount, jwt);

        verify(balanceRepository, times(1)).save(expected);
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountNumber);
        verify(balanceRepository, times(1)).findById(new BalanceId(accountNumber, securityId, securityType));
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getSecurityId(), actual.getSecurityId());
        assertEquals(expected.getSecurityType(), actual.getSecurityType());
        assertEquals(expected.getAmount(), actual.getAmount());
    }

    @Test
    public void createBalance_ThrowsException_IfBalance_AlreadyPresent() throws Exception {
        SecurityType securityType = SecurityType.FOREX;
        int amount = 1;
        Account account = new Account();
        BalanceService balanceServiceSpy = spy(underTest);
        UserDto userDto = new UserDto(1L, "FIRST_NAME", "LAST_NAME", "EMAIL",
                "POSITION", "PHONE_NUMBER", true);
        SecurityDTO securityDTO = new SecurityDTO(1, "TICKER", "NAME", "LAST_UPDATED",
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, 1L, 1,
                new HashSet<SecurityHistoryDTO>(), BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
        Balance balance = new Balance(account, 1L, securityType, 1);

        doReturn(securityDTO).when(balanceServiceSpy).getSecurityByTypeAndId(securityType, securityId, jwt);
        doReturn(userDto).when(balanceServiceSpy).getUserByUsernameFromUserService("USERNAME");
        doReturn("USERNAME").when(balanceServiceSpy).extractUsername(jwt);
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(account);
        when(balanceRepository.findById(new BalanceId(accountNumber, securityId, securityType))).thenReturn(Optional.of(balance));

        Balance expected = new Balance(account, securityId, securityType, amount);

        Exception exception = assertThrows(Exception.class, () -> {
            balanceServiceSpy.createBalance(accountNumber, securityId, securityType, amount, jwt);
        });

        String expectedMessage = "Balance already exist";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void createBalance_ThrowsException_IfAmount_LessThan0() throws Exception {
        SecurityType securityType = SecurityType.FOREX;
        int amount = -1;
        Account account = new Account();
        BalanceService balanceServiceSpy = spy(underTest);
        UserDto userDto = new UserDto(1L, "FIRST_NAME", "LAST_NAME", "EMAIL",
                "POSITION", "PHONE_NUMBER", true);
        SecurityDTO securityDTO = new SecurityDTO(1, "TICKER", "NAME", "LAST_UPDATED",
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, 1L, 1,
                new HashSet<SecurityHistoryDTO>(), BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
        Balance balance = new Balance(account, 1L, securityType, 1);

        doReturn(securityDTO).when(balanceServiceSpy).getSecurityByTypeAndId(securityType, securityId, jwt);
        doReturn(userDto).when(balanceServiceSpy).getUserByUsernameFromUserService("USERNAME");
        doReturn("USERNAME").when(balanceServiceSpy).extractUsername(jwt);
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(account);
        when(balanceRepository.findById(new BalanceId(accountNumber, securityId, securityType))).thenReturn(Optional.empty());

        Balance expected = new Balance(account, securityId, securityType, amount);

        Exception exception = assertThrows(Exception.class, () -> {
            balanceServiceSpy.createBalance(accountNumber, securityId, securityType, amount, jwt);
        });

        String expectedMessage = "Amount is less then zero";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteBalance_ThrowsException_IfNoBalanceFound() {
        SecurityType securityType = SecurityType.CURRENCY;
        BalanceId balanceId = new BalanceId(accountNumber, securityId, securityType);

        when(balanceRepository.findById(balanceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            underTest.deleteBalance(accountNumber, securityId, securityType, jwt);
        });

        String expectedMessage = "Couldn't find balance";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void deleteBalance_CallsBalanceRepositoryDelete_Once() throws Exception {
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = 1;
        Account account = new Account();
        BalanceId balanceId = new BalanceId(accountNumber, securityId, securityType);
        Balance balance = new Balance(account, securityId, securityType, amount);

        when(balanceRepository.findById(balanceId)).thenReturn(Optional.of(balance));

        boolean actual = underTest.deleteBalance(accountNumber, securityId, securityType, jwt);

        assertTrue(actual);
        verify(balanceRepository, times(1)).delete(balance);
    }

    @Test
    public void getAllBalances_CallsBalanceRepositoryFindAll_Once() {
        underTest.getAllBalances();
        verify(balanceRepository, times(1)).findAll();
    }

    @Test
    public void getBalancesByAccount_ReturnsNewArrayList_IfAccountNull() {
        Long accountId = 1L;

        when(accountRepository.findAccountByAccountNumber(accountId)).thenReturn(null);

        List<Balance> actual = underTest.getBalancesByAccount(accountId);

        assertEquals(new ArrayList<>(), actual);
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountId);
    }

    @Test
    public void getBalancesByAccount_CallsBalanceRepositoryFindBalanceByAccount_IfAccountNOTNull() {
        Long accountId = 1L;
        Account account = new Account();

        when(accountRepository.findAccountByAccountNumber(accountId)).thenReturn(account);

        List<Balance> actual = underTest.getBalancesByAccount(accountId);

        verify(accountRepository, times(1)).findAccountByAccountNumber(accountId);
        verify(balanceRepository, times(1)).findBalanceByAccount(account);
    }

    @Test
    public void getBalancesBySecurity_CallsBalanceRepositoryFindBalanceBySecurityId_Once() {
        List<Balance> actual = underTest.getBalancesBySecurity(securityId);
        verify(balanceRepository, times(1)).findBalanceBySecurityId(securityId);
    }

    @Test
    public void getBalancesByAccountAndSecurity_ReturnsEmptyList_IfAccountNull() {
        Long accountId = 1L;

        when(accountRepository.findAccountByAccountNumber(accountId)).thenReturn(null);

        List<Balance> actual = underTest.getBalancesByAccountAndSecurity(accountId, securityId);

        assertEquals(Collections.emptyList(), actual);
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountId);
    }

    @Test
    public void getBalancesByAccountAndSecurity_CallsBalanceRepositoryFindBalanceByAccountIdAndSecurityId_Once() {
        Long accountId = 1L;
        Account account = new Account();

        when(accountRepository.findAccountByAccountNumber(accountId)).thenReturn(account);

        List<Balance> actual = underTest.getBalancesByAccountAndSecurity(accountId, securityId);

        assertEquals(Collections.emptyList(), actual);
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountId);
        verify(balanceRepository, times(1)).findBalanceByAccountIdAndSecurityId(accountId, securityId);
    }


    @Test
    public void getBalancesByFullId_ReturnsEmptyOptional_IfAccountNull() {
        Long accountId = 1L;
        SecurityType securityType = SecurityType.FOREX;

        when(accountRepository.findAccountByAccountNumber(accountId)).thenReturn(null);

        Optional<Balance> actual = underTest.getBalancesByFullId(accountId, securityId, securityType);

        assertEquals(Optional.empty(), actual);
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountId);
    }

    @Test
    public void getBalancesByFullId_CallsBalanceRepositoryFindById_IfAccountNOTNull() {
        Long accountId = 1L;
        SecurityType securityType = SecurityType.FOREX;
        Account account = new Account();

        when(accountRepository.findAccountByAccountNumber(accountId)).thenReturn(account);

        Optional<Balance> actual = underTest.getBalancesByFullId(accountId, securityId, securityType);

        assertEquals(Optional.empty(), actual);
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountId);
        verify(balanceRepository, times(1)).findById(new BalanceId(accountId, securityId, securityType));
    }

    @Test
    public void updateAmount_ThrowsException_IfBalanceOptionalEmpty() {
        BalanceService balanceServiceSpy = spy(underTest);
        Long accountId = 1L;
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = 1;
        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(accountId, securityId, securityType, amount);

        doReturn(Optional.empty()).when(balanceServiceSpy).getBalancesByFullId(balanceUpdateDto.getAccountId(),
                                                                                balanceUpdateDto.getSecurityId(),
                                                                                balanceUpdateDto.getSecurityType());

        Exception exception = assertThrows(Exception.class, () -> {
            balanceServiceSpy.updateAmount(balanceUpdateDto, jwt);
        });

        String expectedMessage = "Couldn't find balance" + balanceUpdateDto.toString();
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateAmount_ThrowsException_IfBalancePlusAmount_IsLessThan0() {
        BalanceService balanceServiceSpy = spy(underTest);
        Long accountId = 1L;
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = -1;
        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(accountId, securityId, securityType, amount);
        Account account = new Account();
        Balance balance = new Balance(account, securityId, securityType, amount);

        doReturn(Optional.of(balance)).when(balanceServiceSpy).getBalancesByFullId(balanceUpdateDto.getAccountId(),
                balanceUpdateDto.getSecurityId(),
                balanceUpdateDto.getSecurityType());

        Exception exception = assertThrows(Exception.class, () -> {
            balanceServiceSpy.updateAmount(balanceUpdateDto, jwt);
        });

        int newAmount = balance.getAmount() + balanceUpdateDto.getAmount();
        String expectedMessage = "Overflow amount: " + newAmount + ". Not enough available";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateAmount_CallsBalanceRepositorySave_Once() throws Exception {
        BalanceService balanceServiceSpy = spy(underTest);
        Long accountId = 1L;
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = 1;
        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(accountId, securityId, securityType, amount);
        Account account = new Account();
        Balance balance = new Balance(account, securityId, securityType, amount);

        doReturn(Optional.of(balance)).when(balanceServiceSpy).getBalancesByFullId(balanceUpdateDto.getAccountId(),
                balanceUpdateDto.getSecurityId(),
                balanceUpdateDto.getSecurityType());

        int newAmount = balance.getAmount() + balanceUpdateDto.getAmount();

        balanceServiceSpy.updateAmount(balanceUpdateDto, jwt);

        Balance expectedBalance = new Balance(account, securityId, securityType, amount);
        expectedBalance.setAmount(newAmount);

        assertEquals(expectedBalance, balance);
        verify(balanceRepository, times(1)).save(expectedBalance);
    }

    @Test
    public void updateReserve_ThrowsException_IfBalanceOptionalEmpty() {
        BalanceService balanceServiceSpy = spy(underTest);
        Long accountId = 1L;
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = 1;
        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(accountId, securityId, securityType, amount);

        doReturn(Optional.empty()).when(balanceServiceSpy).getBalancesByFullId(balanceUpdateDto.getAccountId(),
                balanceUpdateDto.getSecurityId(),
                balanceUpdateDto.getSecurityType());

        Exception exception = assertThrows(Exception.class, () -> {
            balanceServiceSpy.updateReserve(balanceUpdateDto, jwt);
        });

        String expectedMessage = "Couldn't find balance" + balanceUpdateDto.toString();
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateReserve_ThrowsException_IfReservePlusAmount_IsLessThan0() {
        BalanceService balanceServiceSpy = spy(underTest);
        Long accountId = 1L;
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = -1;
        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(accountId, securityId, securityType, amount);
        Account account = new Account();
        Balance balance = new Balance(account, securityId, securityType, amount);

        doReturn(Optional.of(balance)).when(balanceServiceSpy).getBalancesByFullId(balanceUpdateDto.getAccountId(),
                balanceUpdateDto.getSecurityId(),
                balanceUpdateDto.getSecurityType());

        Exception exception = assertThrows(Exception.class, () -> {
            balanceServiceSpy.updateReserve(balanceUpdateDto, jwt);
        });

        int newReserve = balance.getReserved() + balanceUpdateDto.getAmount();
        String expectedMessage = "Overflow amount: " + newReserve;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void updateReserve_CallsBalanceRepositorySave_Once() throws Exception {
        BalanceService balanceServiceSpy = spy(underTest);
        Long accountId = 1L;
        SecurityType securityType = SecurityType.CURRENCY;
        int amount = 1;
        BalanceUpdateDto balanceUpdateDto = new BalanceUpdateDto(accountId, securityId, securityType, amount);
        Account account = new Account();
        Balance balance = new Balance(account, securityId, securityType, amount);

        doReturn(Optional.of(balance)).when(balanceServiceSpy).getBalancesByFullId(balanceUpdateDto.getAccountId(),
                balanceUpdateDto.getSecurityId(),
                balanceUpdateDto.getSecurityType());

        int newReserve = balance.getReserved() + balanceUpdateDto.getAmount();

        balanceServiceSpy.updateReserve(balanceUpdateDto, jwt);

        Balance expectedBalance = new Balance(account, securityId, securityType, amount);
        expectedBalance.setReserved(newReserve);

        assertEquals(expectedBalance, balance);
        verify(balanceRepository, times(1)).save(expectedBalance);
    }
}
