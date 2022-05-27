package buyingmarket.mappers;

import buyingmarket.model.Order;
import buyingmarket.model.dto.OrderDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    private final TransactionMapper transactionMapper;

    @Autowired
    public OrderMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    public Order orderDtoToOrder(OrderDto dto) {
        return Order.builder()
                .orderId(dto.getOrderId())
                .securityId(dto.getSecurityId())
                .userId(dto.getUserId())
                .amount(dto.getAmount())
                .orderType(dto.getOrderType())
                .securityType(dto.getSecurityType())
                .allOrNone(dto.getAllOrNone())
                .margin(dto.getMargin())
                .price(dto.getLimitPrice())
                .stopPrice(dto.getStopPrice())
                .fee(dto.getFee())
                .cost(dto.getCost())
                .active(Boolean.TRUE)
                .build();
    }

    public OrderDto orderToOrderDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .securityId(order.getSecurityId())
                .userId(order.getUserId())
                .amount(order.getAmount())
                .orderType(order.getOrderType())
                .securityType(order.getSecurityType())
                .allOrNone(order.getAllOrNone())
                .margin(order.getMargin())
                .limitPrice(order.getPrice())
                .stopPrice(order.getStopPrice())
                .fee(order.getFee())
                .cost(order.getCost())
                .active(order.getActive())
                .transactions(transactionMapper.transactionsToTransactionDtos(order.getTransactions()))
                .build();
    }

    public List<OrderDto> ordersToOrderDtos(List<Order> orders) {
        List<OrderDto> orderDtos = new ArrayList<>();
        orders.stream().forEach(order -> orderDtos.add(orderToOrderDto(order)));
        return orderDtos;
    }
}

