package buyingmarket.mappers;

import buyingmarket.model.Order;
import buyingmarket.model.SecurityType;
import buyingmarket.model.Transaction;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.model.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void orderDtoToOrderTest(){
        OrderDto orderDto = OrderDto.builder()
                .orderId(Long.valueOf(342))
                .securityId(Long.valueOf(654))
                .amount(Integer.valueOf(324))
                .securityType(SecurityType.STOCKS)
                .userId(Long.valueOf(54))
                .active(Boolean.TRUE)
                .allOrNone(Boolean.FALSE)
                .cost(BigDecimal.valueOf(786))
                .fee(BigDecimal.valueOf(534))
                .margin(BigDecimal.valueOf(879))
                .limitPrice(BigDecimal.valueOf(648))
                .stopPrice(BigDecimal.valueOf(753))
                .transactions(Set.of(TransactionDto.builder().build()))
                .build();
        Order order = orderMapper.orderDtoToOrder(orderDto);
        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isEqualTo(orderDto.getOrderId());
        assertThat(order.getSecurityId()).isEqualTo(orderDto.getSecurityId());
        assertThat(order.getAmount()).isEqualTo(orderDto.getAmount());
        assertThat(order.getSecurityType()).isEqualTo(orderDto.getSecurityType());
        assertThat(order.getUserId()).isEqualTo(orderDto.getUserId());
        assertThat(order.getActive()).isEqualTo(orderDto.getActive());
        assertThat(order.getAllOrNone()).isEqualTo(orderDto.getAllOrNone());
        assertThat(order.getCost()).isEqualTo(orderDto.getCost());
        assertThat(order.getFee()).isEqualTo(orderDto.getFee());
        assertThat(order.getMargin()).isEqualTo(orderDto.getMargin());
        assertThat(order.getLimitPrice()).isEqualTo(orderDto.getLimitPrice());
        assertThat(order.getStopPrice()).isEqualTo(orderDto.getStopPrice());
        assertThat(order.getTransactions()).isNull();
    }

    @Test
    void orderToOrderDtoTest(){
        Order order = Order.builder()
                .orderId(Long.valueOf(342))
                .securityId(Long.valueOf(654))
                .amount(Integer.valueOf(324))
                .securityType(SecurityType.STOCKS)
                .userId(Long.valueOf(54))
                .active(Boolean.TRUE)
                .allOrNone(Boolean.FALSE)
                .cost(BigDecimal.valueOf(786))
                .fee(BigDecimal.valueOf(534))
                .margin(BigDecimal.valueOf(879))
                .limitPrice(BigDecimal.valueOf(648))
                .stopPrice(BigDecimal.valueOf(753))
                .build();
        order.setTransactions(Set.of(Transaction.builder().order(order).build()));
        OrderDto orderDto = orderMapper.orderToOrderDto(order);
        assertThat(orderDto).isNotNull();
        assertThat(orderDto.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(orderDto.getSecurityId()).isEqualTo(order.getSecurityId());
        assertThat(orderDto.getAmount()).isEqualTo(order.getAmount());
        assertThat(orderDto.getSecurityType()).isEqualTo(order.getSecurityType());
        assertThat(orderDto.getUserId()).isEqualTo(order.getUserId());
        assertThat(orderDto.getActive()).isEqualTo(order.getActive());
        assertThat(orderDto.getAllOrNone()).isEqualTo(order.getAllOrNone());
        assertThat(orderDto.getCost()).isEqualTo(order.getCost());
        assertThat(orderDto.getFee()).isEqualTo(order.getFee());
        assertThat(orderDto.getMargin()).isEqualTo(order.getMargin());
        assertThat(orderDto.getLimitPrice()).isEqualTo(order.getLimitPrice());
        assertThat(orderDto.getStopPrice()).isEqualTo(order.getStopPrice());
        assertThat(orderDto.getTransactions()).hasSize(1);
    }

    @Test
    void ordersToOrderDtosTest(){
        Order order1 = Order.builder()
                .orderId(Long.valueOf(342))
                .securityId(Long.valueOf(654))
                .amount(Integer.valueOf(324))
                .securityType(SecurityType.STOCKS)
                .userId(Long.valueOf(54))
                .active(Boolean.TRUE)
                .allOrNone(Boolean.FALSE)
                .cost(BigDecimal.valueOf(786))
                .fee(BigDecimal.valueOf(534))
                .margin(BigDecimal.valueOf(879))
                .limitPrice(BigDecimal.valueOf(648))
                .stopPrice(BigDecimal.valueOf(753))
                .build();
        order1.setTransactions(Set.of(Transaction.builder().order(order1).build()));
        Order order2 = Order.builder()
                .orderId(Long.valueOf(546))
                .securityId(Long.valueOf(867))
                .amount(Integer.valueOf(427))
                .securityType(SecurityType.FOREX)
                .userId(Long.valueOf(957))
                .active(Boolean.FALSE)
                .allOrNone(Boolean.TRUE)
                .cost(BigDecimal.valueOf(926))
                .fee(BigDecimal.valueOf(745))
                .margin(BigDecimal.valueOf(539))
                .limitPrice(BigDecimal.valueOf(852))
                .stopPrice(BigDecimal.valueOf(964))
                .build();
        order2.setTransactions(Set.of(Transaction.builder().order(order2).build()));
        List<Order> orders = List.of(order1, order2);
        List<OrderDto> orderDtos = orderMapper.ordersToOrderDtos(orders);
        assertThat(orderDtos).hasSize(orders.size());
    }
}
