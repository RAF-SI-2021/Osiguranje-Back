package buyingmarket.mappers;

import buyingmarket.model.Order;
import buyingmarket.model.Transaction;
import buyingmarket.model.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionMapperTest {

    @Autowired
    private TransactionMapper transactionMapper;

    @Test
    void transactionToTransactionDtoTest(){
        Order order = Order.builder()
                .orderId(Long.valueOf(1))
                .build();
        Transaction transaction = Transaction.builder()
                .id(Long.valueOf(642))
                .time(LocalDateTime.now())
                .price(BigDecimal.valueOf(268))
                .volume(Long.valueOf(296))
                .order(order)
                .build();
        TransactionDto transactionDto = transactionMapper.transactionToTransactionDto(transaction);
        assertThat(transactionDto).isNotNull();
        assertThat(transactionDto.getId()).isEqualTo(transaction.getId());
        assertThat(transactionDto.getTime()).isEqualTo(transaction.getTime());
        assertThat(transactionDto.getPrice()).isEqualTo(transaction.getPrice());
        assertThat(transactionDto.getVolume()).isEqualTo(transaction.getVolume());
        assertThat(transactionDto.getOrderId()).isEqualTo(order.getOrderId());
    }

    @Test
    void transactionsToTransactionDtosTest(){
        Order order = new Order();
        Transaction transaction1 = Transaction.builder()
                .id(Long.valueOf(753))
                .time(LocalDateTime.now())
                .price(BigDecimal.valueOf(936))
                .volume(Long.valueOf(826))
                .order(order)
                .build();
        Transaction transaction2 = Transaction.builder()
                .id(Long.valueOf(234))
                .time(LocalDateTime.now())
                .price(BigDecimal.valueOf(697))
                .volume(Long.valueOf(420))
                .order(order)
                .build();
        Set<Transaction> transactions = Set.of(transaction1, transaction2);
        Set<TransactionDto> transactionDtos = transactionMapper.transactionsToTransactionDtos(transactions);
        assertThat(transactionDtos).hasSize(transactions.size());
    }
}
