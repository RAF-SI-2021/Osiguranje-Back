import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import raf.osiguranje.accounttransaction.model.Transaction;
import raf.osiguranje.accounttransaction.model.dto.BalanceUpdateDto;
import raf.osiguranje.accounttransaction.model.dto.SecurityType;
import raf.osiguranje.accounttransaction.repositories.TransactionRepository;
import raf.osiguranje.accounttransaction.services.AccountService;
import raf.osiguranje.accounttransaction.services.BalanceService;
import raf.osiguranje.accounttransaction.services.TransactionService;

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
