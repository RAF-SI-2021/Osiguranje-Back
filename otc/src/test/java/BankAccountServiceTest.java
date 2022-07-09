import app.model.BankAccount;
import app.model.dto.BankAccountDTO;
import app.repositories.BankAccountRepository;
import app.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class BankAccountServiceTest {
    private BankAccountRepository bankAccountRepository = mock(BankAccountRepository.class);

    private BankAccountService underTest;

    @BeforeEach
    void setUp() {
        underTest = new BankAccountService(bankAccountRepository);
    }

    @Test
    public void save_CallsBankAccountRepositorySave_Once() {
        BankAccount bankAccount = new BankAccount(new BankAccountDTO(1L, "ACCOUNT_NUMBER",
                                                            "BANK_NAME", "ACOUNT_TYPE"));
        underTest.save(bankAccount);
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    public void deleteByID_CallsBankAccountRepositoryDeleteByID_Once() {
        Long id = 1L;
        underTest.deleteByID(id);
        verify(bankAccountRepository, times(1)).deleteById(id);
    }

    @Test
    public void findById_CallsBankAccountRepositoryFindByID_Once() {
        Long id = 1L;
        underTest.findByID(id);
        verify(bankAccountRepository, times(1)).findById(id);
    }

    @Test
    public void update_CallsBankAccountSetBankName_Once_and_Save_Once() {
        BankAccountService bankAccountServiceSpy = spy(underTest);
        BankAccount bankAccount = mock(BankAccount.class);
        BankAccountDTO bankAccountDTO = mock(BankAccountDTO.class);
        bankAccountServiceSpy.update(bankAccount, bankAccountDTO);
        verify(bankAccount, times(1)).setBankName(bankAccountDTO.getBankName());
        verify(bankAccountServiceSpy, times(1)).save(bankAccount);
    }
}
