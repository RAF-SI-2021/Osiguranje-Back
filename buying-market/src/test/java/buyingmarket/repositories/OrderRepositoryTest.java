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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void init() {
        List<Order> orders = List.of(
                Order.builder()
                        .securityId(Long.valueOf(1))
                        .userId(Long.valueOf(1))
                        .amount(Integer.valueOf(10))
                        .securityType(SecurityType.STOCKS)
                        .limitPrice(BigDecimal.valueOf(420.31))
                        .fee(BigDecimal.valueOf(69.13))
                        .cost(BigDecimal.valueOf(4203.10))
                        .active(Boolean.TRUE)
                        .transactions(new HashSet<>())
                        .build(),
                Order.builder()
                        .securityId(Long.valueOf(2))
                        .userId(Long.valueOf(1))
                        .amount(Integer.valueOf(100))
                        .securityType(SecurityType.FUTURES)
                        .limitPrice(BigDecimal.valueOf(1453.00))
                        .fee(BigDecimal.valueOf(18.18))
                        .cost(BigDecimal.valueOf(145300.))
                        .active(Boolean.FALSE)
                        .transactions(new HashSet<>())
                        .build(),
                Order.builder()
                        .securityId(Long.valueOf(3))
                        .userId(Long.valueOf(2))
                        .amount(Integer.valueOf(1))
                        .securityType(SecurityType.FOREX)
                        .limitPrice(BigDecimal.valueOf(1.1111))
                        .fee(BigDecimal.valueOf(0.01))
                        .cost(BigDecimal.valueOf(1.1111))
                        .active(Boolean.TRUE)
                        .transactions(new HashSet<>())
                        .build()
        );
        orderRepository.saveAll(orders);
    }

    @AfterEach
    public void clear() {
        orderRepository.deleteAll();
    }

    @Test
    public void findAllByUserIdTest() {
        List<Order> firstUserOrders = orderRepository.findAllByUserId(Long.valueOf(1));
        List<Order> secondUserOrders = orderRepository.findAllByUserId(Long.valueOf(2));
        assertThat(firstUserOrders).isNotEmpty();
        assertThat(secondUserOrders).isNotEmpty();
        assertThat(firstUserOrders.size()).isEqualTo(2);
        assertThat(secondUserOrders.size()).isEqualTo(1);
    }

    @Test
    public void findAllByUserIdAndActiveTest() {
        List<Order> firstUserAndActiveOrders = orderRepository.findAllByUserIdAndActive(Long.valueOf(1), Boolean.TRUE);
        List<Order> firstUserAndInactiveOrders = orderRepository.findAllByUserIdAndActive(Long.valueOf(1), Boolean.FALSE);
        List<Order> secondUserAndActiveOrders = orderRepository.findAllByUserIdAndActive(Long.valueOf(2), Boolean.TRUE);
        List<Order> secondUserAndInactiveOrders = orderRepository.findAllByUserIdAndActive(Long.valueOf(2), Boolean.FALSE);
        assertThat(firstUserAndActiveOrders).isNotEmpty();
        assertThat(firstUserAndInactiveOrders).isNotEmpty();
        assertThat(secondUserAndActiveOrders).isNotEmpty();
        assertThat(secondUserAndInactiveOrders).isEmpty();
        assertThat(firstUserAndActiveOrders.size()).isEqualTo(1);
        assertThat(firstUserAndInactiveOrders.size()).isEqualTo(1);
        assertThat(secondUserAndActiveOrders.size()).isEqualTo(1);
    }

    @Test
    public void findByOrderIdAndUserIdTest(){
        List<Order> allOrders = orderRepository.findAll();
        allOrders.stream().map(order -> order.getOrderId()).forEach(orderId -> {
            Optional<Order> orderForFirstOptional = orderRepository.findByOrderIdAndUserId(orderId, Long.valueOf(1));
            Optional<Order> orderForSecondOptional = orderRepository.findByOrderIdAndUserId(orderId, Long.valueOf(2));
            boolean isPresentForFirst = orderForFirstOptional.isPresent();
            boolean isPresentForSecond = orderForSecondOptional.isPresent();
            assertThat(isPresentForFirst).isNotEqualTo(isPresentForSecond);
        });
    }
}
