package buyingmarket.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buyingmarket.model.Order;
import buyingmarket.model.SecurityType;
import buyingmarket.model.Transaction;
import buyingmarket.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TransactionService.class})
@ExtendWith(SpringExtension.class)
class TransactionServiceTest {
    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    /**
     * Method under test: {@link TransactionService#save(Transaction)}
     */
    @Test
    void testSave() {
        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);

        Transaction transaction = new Transaction();
        transaction.setId(123L);
        transaction.setOrder(order);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(1L);
        when(this.transactionRepository.save((Transaction) any())).thenReturn(transaction);

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);

        Transaction transaction1 = new Transaction();
        transaction1.setId(123L);
        transaction1.setOrder(order1);
        transaction1.setPrice(BigDecimal.valueOf(42L));
        transaction1.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction1.setVolume(1L);
        Transaction actualSaveResult = this.transactionService.save(transaction1);
        assertSame(transaction, actualSaveResult);
        assertEquals("42", actualSaveResult.getPrice().toString());
        Order order2 = actualSaveResult.getOrder();
        assertEquals("42", order2.getLimitPrice().toString());
        assertEquals("42", order2.getCost().toString());
        assertEquals("42", order2.getStopPrice().toString());
        assertEquals("42", order2.getMargin().toString());
        assertEquals("42", order2.getFee().toString());
        verify(this.transactionRepository).save((Transaction) any());
    }
}

