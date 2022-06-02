package buyingmarket.repositories;

import buyingmarket.model.Order;
import buyingmarket.model.SecurityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void init() {
        Order firstOrder = Order.builder()
                .securityId(Long.valueOf(1))
                .userId(Long.valueOf(1))
                .amount(Integer.valueOf(10))
                .securityType(SecurityType.STOCKS)
                .limitPrice(BigDecimal.valueOf(420.31))
                .fee(BigDecimal.valueOf(69.13))
                .cost(BigDecimal.valueOf(4203.10))
                .active(Boolean.TRUE)
                .transactions(new HashSet<>())
                .build();
        orderRepository.save(firstOrder);
    }

    @AfterEach
    public void clear() {
        orderRepository.deleteAll();
    }

    @Test
    public void findAllTest() {
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).isNotEmpty();
    }
}
