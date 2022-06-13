package buyingmarket.exceptions;

import buyingmarket.mappers.OrderMapper;
import buyingmarket.model.Order;
import buyingmarket.model.SecurityType;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.model.dto.UserDto;
import buyingmarket.repositories.OrderRepository;
import buyingmarket.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionTest {

    @Spy
    private OrderRepository orderRepository;

    @InjectMocks
    @Spy
    private OrderService orderService;

    @Spy
    private OrderMapper orderMapper;

    @Mock
    private RestTemplate rest;

    @Test
    void orderNotFoundExceptionTest() {
        doReturn("").when(orderService).extractUsername("");
        UserDto user = new UserDto();
        user.setId(Long.valueOf(1));
        doReturn(user).when(orderService).getUserByUsernameFromUserService("");
        doReturn(Optional.empty()).when(orderRepository).findByOrderIdAndUserId(Long.valueOf(1), Long.valueOf(1));
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.findOrderForUser(Long.valueOf(1), "");
        });
    }

//    @Test
//    void securityNotFoundExceptionTest() {
//        doReturn("").when(orderService).extractUsername("");
//        UserDto user = new UserDto();
//        user.setId(Long.valueOf(1));
//        doReturn(user).when(orderService).getUserByUsernameFromUserService("");
//        OrderDto orderDto = OrderDto.builder()
//                .securityType(SecurityType.STOCKS)
//                .securityId(Long.valueOf(1))
//                .build();
//        lenient().doThrow(new RestClientException("")).when(rest).exchange(any(),
//                any(),
//                any(),
//                (Class<Object>) any());
//        assertThrows(SecurityNotFoundException.class, () -> {
//            orderService.createOrder(orderDto, "");
//        });
//    }

    @Test
    void updateNotAllowedException() {
        doReturn("").when(orderService).extractUsername("");
        UserDto user = new UserDto();
        user.setId(Long.valueOf(1));
        doReturn(user).when(orderService).getUserByUsernameFromUserService("");
        OrderDto orderDto = OrderDto.builder()
                .orderId(Long.valueOf(1))
                .build();
        Optional<Order> optionalOrder = Optional.of(Order.builder().build());
        doReturn(optionalOrder).when(orderRepository).findByOrderIdAndUserId(Long.valueOf(1), Long.valueOf(1));
        assertThrows(UpdateNotAllowedException.class, () -> {
            orderService.updateOrder(orderDto, "");
        });
    }

//    @Test
//    void userNotFoundExceptionTest() {
//        lenient().doThrow(new RestClientException("")).when(rest).exchange(any(),
//                any(),
//                any(),
//                (Class<Object>) any());
//        assertThrows(UserNotFoundException.class, () -> {
//            orderService.getUserByUsernameFromUserService("");
//        });
//    }
}
