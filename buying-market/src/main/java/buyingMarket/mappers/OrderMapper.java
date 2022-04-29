package buyingMarket.mappers;

import buyingMarket.model.Order;
import buyingMarket.model.dto.OrderCreateDto;
import buyingMarket.model.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public Order orderCreateDtoToOrder(OrderCreateDto dto) {
        return new Order(dto.getSecurityId(), dto.getAmount(), dto.getOrderType(), dto.getSecurityType(), dto.isAllOrNone(), dto.isMargin());
    }

    public OrderDto orderToOrderDto(Order order) {
        return new OrderDto(order.getOrderId(), order.getSecurityId(), order.getAmount(), order.getOrderType(), order.getSecurityType(), order.isAllOrNone(), order.isMargin());
    }
}
