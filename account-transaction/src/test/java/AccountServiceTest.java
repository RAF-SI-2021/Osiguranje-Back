import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raf.osiguranje.accounttransaction.model.Account;
import raf.osiguranje.accounttransaction.model.AccountType;
import raf.osiguranje.accounttransaction.repositories.AccountRepository;
import raf.osiguranje.accounttransaction.services.AccountService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountRepository accountRepository = mock(AccountRepository.class);

    private AccountService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccountService(accountRepository);
    }

    @Test
    public void createNewAccount_CallsAccountRepositorySave_Once() {
        Account actual = underTest.createNewAccount();

        verify(accountRepository, times(1)).save(new Account());
    }

    @Test
    public void createNewAccount_CallsAccountRepositorySave_Once_WithAccountType() {
        AccountType accountType = AccountType.MARGINS;

        Account actual = underTest.createNewAccount(accountType);

        verify(accountRepository, times(1)).save(new Account(accountType));
    }

    @Test
    public void deleteAccount_CallsAccountRepositoryDelete_Once() {
        Account account = new Account();

        underTest.deleteAccount(account);

        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    public void getAllAccounts_CallsAccountRepositoryFindAll_Once() {
        ArrayList accountList = new ArrayList<Account>();
        accountList.add(new Account());
        accountList.add(new Account(AccountType.MARGINS));

        when(accountRepository.findAll()).thenReturn(accountList);

        ArrayList actual = (ArrayList) underTest.getAllAccounts();

        verify(accountRepository, times(1)).findAll();
        assertEquals(accountList, actual);
    }

    @Test
    public void findAccountById_CallsAccountRepositoryFindAccountByAccountNumber_Once() {
        Long id = 1L;

        underTest.findAccountById(id);

        verify(accountRepository, times(1)).findAccountByAccountNumber(id);
    }

    @Test
    public void findAccountByAccountType_CallsAccountRepositoryFindAccountByAccountType_Once() {
         underTest.findAccountByAccountType(AccountType.CASH);

        verify(accountRepository, times(1)).findAccountByAccountType(AccountType.CASH);
    }
}
