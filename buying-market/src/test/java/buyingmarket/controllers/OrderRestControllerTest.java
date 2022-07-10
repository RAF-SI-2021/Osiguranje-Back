package buyingmarket.controllers;

import buyingmarket.exceptions.UserNotFoundException;
import buyingmarket.model.ActionType;
import buyingmarket.model.OrderState;
import buyingmarket.model.SecurityType;
import buyingmarket.model.dto.OrderCreateDto;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith( MockitoExtension.class )
public class OrderRestControllerTest {

    @Mock
    private OrderService orderService;

    private OrderRestController underTest;


    @BeforeEach
    void setUp() {
        underTest = new OrderRestController(orderService);
    }

    private List<OrderDto> getDummyOrderDto(){
        List<OrderDto> orderDtos = new ArrayList<>();
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(10L);
        orderDto.setOrderState(OrderState.APPROVED);
        orderDto.setActionType(ActionType.BUY);
        orderDto.setAmount(100);
        orderDto.setSecurityId(167L);
        orderDto.setSecurityType(SecurityType.FOREX);
        orderDtos.add(orderDto);

        orderDto = new OrderDto();
        orderDto.setOrderId(11L);
        orderDto.setOrderState(OrderState.APPROVED);
        orderDto.setActionType(ActionType.BUY);
        orderDto.setAmount(100);
        orderDto.setSecurityId(127L);
        orderDto.setSecurityType(SecurityType.FOREX);
        orderDtos.add(orderDto);

        orderDto = new OrderDto();
        orderDto.setOrderId(12L);
        orderDto.setOrderState(OrderState.APPROVED);
        orderDto.setActionType(ActionType.SELL);
        orderDto.setAmount(100);
        orderDto.setSecurityId(267L);
        orderDto.setSecurityType(SecurityType.FOREX);
        orderDtos.add(orderDto);

        return orderDtos;
    }


    @Test
    public void createOrderTest(){
        final String JWT = "jwt";

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setAmount(100);
        orderCreateDto.setSecurityId(167L);
        orderCreateDto.setSecurityType(SecurityType.FOREX);

        ResponseEntity<?> result = underTest.createOrder( orderCreateDto,JWT );

        assertEquals(result.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void createOrderTestException(){
        final String JWT = "jwt";

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setAmount(100);
        orderCreateDto.setSecurityId(167L);
        orderCreateDto.setSecurityType(SecurityType.FOREX);

        try {
            Mockito.doThrow(new UserNotFoundException("No actuary found")).when(orderService).createOrder(orderCreateDto,JWT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<?> result = underTest.createOrder( orderCreateDto,JWT );

        assertEquals(result.getStatusCode(), HttpStatus.OK);

    }


    @Test
    public void findAllTest(){
        final String NAME = "TEST_NAME";
        final String JWT = "jwt";

        List<OrderDto> orderDtos = getDummyOrderDto();

        when(orderService.findAllOrdersForUser(JWT)).thenReturn(orderDtos);

        ResponseEntity<?> result = underTest.findAll( JWT );

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        Object body = result.getBody();
        assertTrue(body instanceof List);

        List<OrderDto> resultBody = (List<OrderDto>) body;
        assertEquals(3, resultBody.size());
        OrderDto orderMini = resultBody.get(0);
        assertEquals(10L, (long) orderMini.getOrderId());
        assertEquals(orderMini.getOrderState(),OrderState.APPROVED);

    }

    @Test
    public void findAllTestException(){
        final String NAME = "TEST_NAME";
        final String JWT = "jwt";


        when(orderService.findAllOrdersForUser(JWT)).thenThrow(new UserNotFoundException("No actuary found"));

        ResponseEntity<?> result = underTest.findAll( JWT );

        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Object body = result.getBody();
        assertTrue(body instanceof String);
        assertEquals(body,"No actuary found");

    }

    @Test
    public void findOrderTest(){
        final long ID = 100;
        final String JWT = "jwt";

        OrderDto orderDto = getDummyOrderDto().get(0);

        when(orderService.findOrderForUser(ID,JWT)).thenReturn(orderDto);

        ResponseEntity<?> result = underTest.findOrder( ID, JWT );

        assertEquals(result.getStatusCode(),HttpStatus.OK);

        Object body = result.getBody();
        assertTrue(body instanceof OrderDto);

    }

    @Test
    public void deleteOrderTest(){
        final long ID = 100;
        final String JWT = "jwt";

        ResponseEntity<?> result = underTest.deleteOrder( ID, JWT );

        assertEquals(result.getStatusCode(),HttpStatus.OK);

    }

    @Test
    public void deleteAllOrderTest(){
        final long ID = 100;
        final String JWT = "jwt";

        ResponseEntity<?> result = underTest.deleteAll( JWT );

        assertEquals(result.getStatusCode(),HttpStatus.OK);

    }

    @Test
    public void validateOrderTest(){
        final long ID = 100;
        final String JWT = "jwt";
        final OrderState orderState = OrderState.APPROVED;

        ResponseEntity<?> result = underTest.validateOrder(ID,orderState, JWT );

        assertEquals(result.getStatusCode(),HttpStatus.NO_CONTENT);

    }

}
