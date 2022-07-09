import app.model.Company;
import app.model.Contract;
import app.model.Status;
import app.model.TransactionItem;
import app.model.dto.*;
import app.repositories.ContractRepository;
import app.services.ContractService;
import app.services.TransactionItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ContractServiceTest {
    private ContractRepository contractRepository = mock(ContractRepository.class);
    private TransactionItemService transactionItemService = mock(TransactionItemService.class);

    private ContractService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ContractService(contractRepository, transactionItemService);
    }

    @Test
    public void save_CallsContractRepositorySave_Once() {
        Contract contract = new Contract(new CreateContractDTO(1L, "REF_NUMBER", "DESCRIPTION"));
        underTest.save(contract);
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    public void findAll_CallsContractRepositoryFindAll_Once() {
        underTest.findAll();
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    public void findById_CallsContractRepositoryFindById_Once() {
        Long id = 1L;
        underTest.findByID(id);
        verify(contractRepository, times(1)).findById(id);
    }

    @Test
    public void createTransactionItem_callsContractRepositorySave_Once() {
        Contract contract = new Contract(1L, new Company(new CompanyDTO(1L, 1L, "NAME",
                1L, 1L, "ADDRESS",
                new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>())),
                Status.DRAFT, "CREATION_DATE", "LAST_UPDATED", "REF_NUMBER",
                "DESCRIPTION", new ArrayList<>());
        TransactionItem transactionItem = new TransactionItem(new TransactionItemDTO());
        underTest.createTransactionItem(contract, transactionItem);
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    public void deleteTransactionItem_callsContractRepositorySave_Once_AndTransactionItemServiceDeleteById_Once() {
        Contract contract = new Contract(1L, new Company(new CompanyDTO(1L, 1L, "NAME",
                1L, 1L, "ADDRESS",
                new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>())),
                Status.DRAFT, "CREATION_DATE", "LAST_UPDATED", "REF_NUMBER",
                "DESCRIPTION", new ArrayList<>());
        Long transactionID = 1L;
        underTest.deleteTransactionItem(contract, transactionID);
        verify(contractRepository, times(1)).save(contract);
        verify(transactionItemService, times(1)).deleteByID(transactionID);
    }

    @Test
    public void updateTransactionItem_callsContractRepositorySave_Once_AndTransactionItemServiceUpdate_Once() {
        Contract contract = new Contract(1L, new Company(new CompanyDTO(1L, 1L, "NAME",
                1L, 1L, "ADDRESS",
                new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>())),
                Status.DRAFT, "CREATION_DATE", "LAST_UPDATED", "REF_NUMBER",
                "DESCRIPTION", new ArrayList<>());
        TransactionItemDTO transactionItemDTO = new TransactionItemDTO();
        underTest.updateTransactionItem(contract, transactionItemDTO);
        verify(contractRepository, times(1)).save(contract);
        verify(transactionItemService, times(1)).update(transactionItemDTO);
    }

    @Test
    public void finalizeContract_callsContractRepositorySave_Once() {
        Contract contract = new Contract(1L, new Company(new CompanyDTO(1L, 1L, "NAME",
                1L, 1L, "ADDRESS",
                new ArrayList<EmployeeDTO>(), new ArrayList<BankAccountDTO>())),
                Status.DRAFT, "CREATION_DATE", "LAST_UPDATED", "REF_NUMBER",
                "DESCRIPTION", new ArrayList<>());

        underTest.finalizeContract(contract);
        verify(contractRepository, times(1)).save(contract);
        assertEquals(Status.FINALIZED, contract.getStatus());
    }
}
