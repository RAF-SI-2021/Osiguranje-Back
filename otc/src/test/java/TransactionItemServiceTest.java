import app.model.TransactionItem;
import app.model.TransactionType;
import app.model.api.SecurityType;
import app.model.dto.TransactionItemDTO;
import app.repositories.TransactionItemRepository;
import app.services.TransactionItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionItemServiceTest {

    private TransactionItemRepository transactionItemRepository = mock(TransactionItemRepository.class);

    private TransactionItemService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TransactionItemService(transactionItemRepository);
    }

    @Test
    public void findById_CallsTransactionItemRepositoryFindById_Once() {
        Long id = 1L;
        underTest.findByID(id);
        verify(transactionItemRepository, times(1)).findById(id);
    }

    @Test
    public void deleteByID_CallsTransactionItemRepositoryDeleteById_Once() {
        Long id = 1L;
        underTest.deleteByID(id);
        verify(transactionItemRepository, times(1)).deleteById(id);
    }

    @Test
    public void update_Returns_IfTransactionItemIsNull() {
        TransactionItemDTO transactionItemDTO = new TransactionItemDTO(new TransactionItem(1L, TransactionType.BUY,
                                                                                    1L, SecurityType.CURRENCY,
                                                                                    1L, 1L, 1,
                                                                                1.0));

        when(transactionItemRepository.findById(transactionItemDTO.getId())).thenReturn(Optional.empty());

        underTest.update(transactionItemDTO);

        verify(transactionItemRepository, times(1)).findById(transactionItemDTO.getId());
        verify(transactionItemRepository, times(0)).save(any());
    }

    @Test
    public void update_CallsCompanyRepositorySave_Once() {
        TransactionItemDTO transactionItemDTO = new TransactionItemDTO(new TransactionItem(1L, TransactionType.BUY,
                1L, SecurityType.CURRENCY,
                1L, 1L, 1,
                1.0));
        TransactionItemDTO transactionItemDTO2 = new TransactionItemDTO(new TransactionItem(2L, TransactionType.SELL,
                2L, SecurityType.FOREX,
                2L, 2L, 2,
                2.0));

        TransactionItem transactionItem = new TransactionItem(transactionItemDTO2);

        TransactionItem expectedTransactionItem = new TransactionItem(transactionItemDTO2);
        expectedTransactionItem.setTransactionType(transactionItemDTO.getTransactionType());
        expectedTransactionItem.setSecurityId(transactionItemDTO.getSecurityId());
        expectedTransactionItem.setSecurityType(transactionItemDTO.getSecurityType());
        expectedTransactionItem.setAccountId(transactionItemDTO.getAccountId());
        expectedTransactionItem.setCurrencyId(transactionItemDTO.getCurrencyId());
        expectedTransactionItem.setAmount(transactionItemDTO.getAmount());
        expectedTransactionItem.setPricePerShare(transactionItemDTO.getPricePerShare());

        when(transactionItemRepository.findById(transactionItemDTO.getId())).thenReturn(Optional.of(transactionItem));

        underTest.update(transactionItemDTO);

        verify(transactionItemRepository, times(1)).findById(transactionItemDTO.getId());
        verify(transactionItemRepository, times(1)).save(transactionItem);
        assertEquals(expectedTransactionItem.getTransactionType(), transactionItem.getTransactionType());
        assertEquals(expectedTransactionItem.getSecurityId(), transactionItem.getSecurityId());
        assertEquals(expectedTransactionItem.getSecurityType(), transactionItem.getSecurityType());
        assertEquals(expectedTransactionItem.getAccountId(), transactionItem.getAccountId());
        assertEquals(expectedTransactionItem.getCurrencyId(), transactionItem.getCurrencyId());
        assertEquals(expectedTransactionItem.getAmount(), transactionItem.getAmount());
        assertEquals(expectedTransactionItem.getPricePerShare(), transactionItem.getPricePerShare());
    }
}
