package buyingmarket.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buyingmarket.model.ActionType;
import buyingmarket.model.Actuary;
import buyingmarket.model.ActuaryType;
import buyingmarket.model.Order;
import buyingmarket.model.OrderState;
import buyingmarket.model.SecurityType;
import buyingmarket.model.Supervisor;
import buyingmarket.model.dto.OrderCreateDto;
import buyingmarket.model.dto.OrderDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderMapper.class})
@ExtendWith(SpringExtension.class)
class OrderMapperTest {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * Method under test: {@link OrderMapper#orderCreateDtoToOrder(OrderCreateDto)}
     */
    @Test
    void testOrderCreateDtoToOrder() {
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setActionType(ActionType.BUY);
        orderCreateDto.setAllOrNone(true);
        orderCreateDto.setAmount(10);
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        orderCreateDto.setLimitPrice(valueOfResult);
        BigDecimal valueOfResult1 = BigDecimal.valueOf(42L);
        orderCreateDto.setMargin(valueOfResult1);
        orderCreateDto.setSecurityId(123L);
        orderCreateDto.setSecurityType(SecurityType.STOCKS);
        orderCreateDto.setStopPrice(BigDecimal.valueOf(42L));
        Order actualOrderCreateDtoToOrderResult = this.orderMapper.orderCreateDtoToOrder(orderCreateDto);
        assertEquals(ActionType.BUY, actualOrderCreateDtoToOrderResult.getActionType());
        assertNull(actualOrderCreateDtoToOrderResult.getUserId());
        assertNull(actualOrderCreateDtoToOrderResult.getTransactions());
        BigDecimal stopPrice = actualOrderCreateDtoToOrderResult.getStopPrice();
        assertEquals(valueOfResult, stopPrice);
        assertEquals(SecurityType.STOCKS, actualOrderCreateDtoToOrderResult.getSecurityType());
        assertNull(actualOrderCreateDtoToOrderResult.getActuary());
        assertEquals(10, actualOrderCreateDtoToOrderResult.getAmount().intValue());
        BigDecimal limitPrice = actualOrderCreateDtoToOrderResult.getLimitPrice();
        assertEquals(valueOfResult1, limitPrice);
        assertEquals(123L, actualOrderCreateDtoToOrderResult.getSecurityId().longValue());
        BigDecimal margin = actualOrderCreateDtoToOrderResult.getMargin();
        assertEquals(limitPrice, margin);
        assertEquals(0, actualOrderCreateDtoToOrderResult.getAmountFilled().intValue());
        assertNull(actualOrderCreateDtoToOrderResult.getOrderId());
        assertTrue(actualOrderCreateDtoToOrderResult.getAllOrNone());
        assertNull(actualOrderCreateDtoToOrderResult.getApprovingActuary());
        assertEquals(OrderState.APPROVED, actualOrderCreateDtoToOrderResult.getOrderState());
        assertNull(actualOrderCreateDtoToOrderResult.getFee());
        assertEquals("42", limitPrice.toString());
        assertEquals("42", stopPrice.toString());
        assertEquals("42", margin.toString());
    }

    /**
     * Method under test: {@link OrderMapper#orderCreateDtoToOrder(OrderCreateDto)}
     */
    @Test
    void testOrderCreateDtoToOrder2() {
        OrderCreateDto orderCreateDto = mock(OrderCreateDto.class);
        when(orderCreateDto.getActionType()).thenReturn(ActionType.BUY);
        when(orderCreateDto.getSecurityType()).thenReturn(SecurityType.STOCKS);
        when(orderCreateDto.getAllOrNone()).thenReturn(true);
        when(orderCreateDto.getAmount()).thenReturn(10);
        when(orderCreateDto.getSecurityId()).thenReturn(123L);
        when(orderCreateDto.getLimitPrice()).thenReturn(BigDecimal.valueOf(42L));
        when(orderCreateDto.getMargin()).thenReturn(BigDecimal.valueOf(42L));
        when(orderCreateDto.getStopPrice()).thenReturn(BigDecimal.valueOf(42L));
        doNothing().when(orderCreateDto).setActionType((ActionType) any());
        doNothing().when(orderCreateDto).setAllOrNone((Boolean) any());
        doNothing().when(orderCreateDto).setAmount((Integer) any());
        doNothing().when(orderCreateDto).setLimitPrice((BigDecimal) any());
        doNothing().when(orderCreateDto).setMargin((BigDecimal) any());
        doNothing().when(orderCreateDto).setSecurityId((Long) any());
        doNothing().when(orderCreateDto).setSecurityType((SecurityType) any());
        doNothing().when(orderCreateDto).setStopPrice((BigDecimal) any());
        orderCreateDto.setActionType(ActionType.BUY);
        orderCreateDto.setAllOrNone(true);
        orderCreateDto.setAmount(10);
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        orderCreateDto.setLimitPrice(valueOfResult);
        orderCreateDto.setMargin(BigDecimal.valueOf(42L));
        orderCreateDto.setSecurityId(123L);
        orderCreateDto.setSecurityType(SecurityType.STOCKS);
        orderCreateDto.setStopPrice(BigDecimal.valueOf(42L));
        Order actualOrderCreateDtoToOrderResult = this.orderMapper.orderCreateDtoToOrder(orderCreateDto);
        assertEquals(ActionType.BUY, actualOrderCreateDtoToOrderResult.getActionType());
        assertNull(actualOrderCreateDtoToOrderResult.getUserId());
        assertNull(actualOrderCreateDtoToOrderResult.getTransactions());
        BigDecimal stopPrice = actualOrderCreateDtoToOrderResult.getStopPrice();
        assertEquals(valueOfResult, stopPrice);
        assertEquals(SecurityType.STOCKS, actualOrderCreateDtoToOrderResult.getSecurityType());
        assertNull(actualOrderCreateDtoToOrderResult.getActuary());
        assertEquals(10, actualOrderCreateDtoToOrderResult.getAmount().intValue());
        BigDecimal limitPrice = actualOrderCreateDtoToOrderResult.getLimitPrice();
        assertEquals(valueOfResult, limitPrice);
        assertEquals(123L, actualOrderCreateDtoToOrderResult.getSecurityId().longValue());
        BigDecimal margin = actualOrderCreateDtoToOrderResult.getMargin();
        assertEquals(valueOfResult, margin);
        assertEquals(0, actualOrderCreateDtoToOrderResult.getAmountFilled().intValue());
        assertNull(actualOrderCreateDtoToOrderResult.getOrderId());
        assertTrue(actualOrderCreateDtoToOrderResult.getAllOrNone());
        assertNull(actualOrderCreateDtoToOrderResult.getApprovingActuary());
        assertEquals(OrderState.APPROVED, actualOrderCreateDtoToOrderResult.getOrderState());
        assertNull(actualOrderCreateDtoToOrderResult.getFee());
        assertEquals("42", limitPrice.toString());
        assertEquals("42", stopPrice.toString());
        assertEquals("42", margin.toString());
        verify(orderCreateDto).getActionType();
        verify(orderCreateDto).getSecurityType();
        verify(orderCreateDto).getAllOrNone();
        verify(orderCreateDto).getAmount();
        verify(orderCreateDto).getSecurityId();
        verify(orderCreateDto).getLimitPrice();
        verify(orderCreateDto).getMargin();
        verify(orderCreateDto).getStopPrice();
        verify(orderCreateDto).setActionType((ActionType) any());
        verify(orderCreateDto).setAllOrNone((Boolean) any());
        verify(orderCreateDto).setAmount((Integer) any());
        verify(orderCreateDto).setLimitPrice((BigDecimal) any());
        verify(orderCreateDto).setMargin((BigDecimal) any());
        verify(orderCreateDto).setSecurityId((Long) any());
        verify(orderCreateDto).setSecurityType((SecurityType) any());
        verify(orderCreateDto).setStopPrice((BigDecimal) any());
    }

    /**
     * Method under test: {@link OrderMapper#orderToOrderDto(Order)}
     */
    @Test
    void testOrderToOrderDto() {
        Actuary actuary = new Actuary();
        actuary.setActive(true);
        actuary.setActuaryType(ActuaryType.SUPERVISOR);
        actuary.setApprovalRequired(true);
        actuary.setId(123L);
        actuary.setOrders(new ArrayList<>());
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        actuary.setSpendingLimit(valueOfResult);
        actuary.setUsedLimit(BigDecimal.valueOf(42L));
        actuary.setUserId(123L);
        actuary.setVersion(1);

        Supervisor supervisor = new Supervisor();
        supervisor.setActive(true);
        supervisor.setActuaryType(ActuaryType.SUPERVISOR);
        supervisor.setApprovalRequired(true);
        supervisor.setId(123L);
        supervisor.setOrders(new ArrayList<>());
        supervisor.setSpendingLimit(BigDecimal.valueOf(42L));
        supervisor.setUsedLimit(BigDecimal.valueOf(42L));
        supervisor.setUserId(123L);
        supervisor.setVersion(1);

        Order order = new Order();
        order.setActionType(ActionType.BUY);
        order.setActuary(actuary);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setAmountFilled(10);
        order.setApprovingActuary(supervisor);
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        order.setModificationDate(fromResult);
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderDto actualOrderToOrderDtoResult = this.orderMapper.orderToOrderDto(order);
        assertNull(actualOrderToOrderDtoResult.getActionType());
        assertEquals(123L, actualOrderToOrderDtoResult.getUserId().longValue());
        assertTrue(actualOrderToOrderDtoResult.getTransactions().isEmpty());
        BigDecimal stopPrice = actualOrderToOrderDtoResult.getStopPrice();
        assertEquals(valueOfResult, stopPrice);
        assertEquals(SecurityType.STOCKS, actualOrderToOrderDtoResult.getSecurityType());
        assertTrue(actualOrderToOrderDtoResult.getAllOrNone());
        assertNull(actualOrderToOrderDtoResult.getAmountFilled());
        assertSame(fromResult, actualOrderToOrderDtoResult.getModificationDate());
        assertEquals(123L, actualOrderToOrderDtoResult.getSecurityId().longValue());
        assertEquals(OrderState.APPROVED, actualOrderToOrderDtoResult.getOrderState());
        assertEquals(123L, actualOrderToOrderDtoResult.getOrderId().longValue());
        BigDecimal fee = actualOrderToOrderDtoResult.getFee();
        assertEquals(valueOfResult, fee);
        BigDecimal margin = actualOrderToOrderDtoResult.getMargin();
        assertEquals(valueOfResult, margin);
        assertEquals(10, actualOrderToOrderDtoResult.getAmount().intValue());
        BigDecimal limitPrice = actualOrderToOrderDtoResult.getLimitPrice();
        assertEquals(valueOfResult, limitPrice);
        assertEquals("42", limitPrice.toString());
        assertEquals("42", fee.toString());
        assertEquals("42", margin.toString());
        assertEquals("42", stopPrice.toString());
    }

    /**
     * Method under test: {@link OrderMapper#orderToOrderDto(Order)}
     */
    @Test
    void testOrderToOrderDto2() {
        Actuary actuary = new Actuary();
        actuary.setActive(true);
        actuary.setActuaryType(ActuaryType.SUPERVISOR);
        actuary.setApprovalRequired(true);
        actuary.setId(123L);
        actuary.setOrders(new ArrayList<>());
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        actuary.setSpendingLimit(valueOfResult);
        actuary.setUsedLimit(BigDecimal.valueOf(42L));
        actuary.setUserId(123L);
        actuary.setVersion(1);

        Supervisor supervisor = new Supervisor();
        supervisor.setActive(true);
        supervisor.setActuaryType(ActuaryType.SUPERVISOR);
        supervisor.setApprovalRequired(true);
        supervisor.setId(123L);
        supervisor.setOrders(new ArrayList<>());
        supervisor.setSpendingLimit(BigDecimal.valueOf(42L));
        supervisor.setUsedLimit(BigDecimal.valueOf(42L));
        supervisor.setUserId(123L);
        supervisor.setVersion(1);
        Order order = mock(Order.class);
        when(order.getOrderState()).thenReturn(OrderState.APPROVED);
        when(order.getSecurityType()).thenReturn(SecurityType.STOCKS);
        when(order.getAllOrNone()).thenReturn(true);
        when(order.getAmount()).thenReturn(10);
        when(order.getOrderId()).thenReturn(123L);
        when(order.getSecurityId()).thenReturn(123L);
        when(order.getUserId()).thenReturn(123L);
        when(order.getFee()).thenReturn(BigDecimal.valueOf(42L));
        when(order.getLimitPrice()).thenReturn(BigDecimal.valueOf(42L));
        when(order.getMargin()).thenReturn(BigDecimal.valueOf(42L));
        when(order.getStopPrice()).thenReturn(BigDecimal.valueOf(42L));
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult = Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        when(order.getModificationDate()).thenReturn(fromResult);
        when(order.getTransactions()).thenReturn(new HashSet<>());
        doNothing().when(order).setActionType((ActionType) any());
        doNothing().when(order).setActuary((Actuary) any());
        doNothing().when(order).setAllOrNone((Boolean) any());
        doNothing().when(order).setAmount((Integer) any());
        doNothing().when(order).setAmountFilled((Integer) any());
        doNothing().when(order).setApprovingActuary((Supervisor) any());
        doNothing().when(order).setFee((BigDecimal) any());
        doNothing().when(order).setLimitPrice((BigDecimal) any());
        doNothing().when(order).setMargin((BigDecimal) any());
        doNothing().when(order).setModificationDate((Date) any());
        doNothing().when(order).setOrderId((Long) any());
        doNothing().when(order).setOrderState((OrderState) any());
        doNothing().when(order).setSecurityId((Long) any());
        doNothing().when(order).setSecurityType((SecurityType) any());
        doNothing().when(order).setStopPrice((BigDecimal) any());
        doNothing().when(order).setTransactions((Set<Long>) any());
        doNothing().when(order).setUserId((Long) any());
        order.setActionType(ActionType.BUY);
        order.setActuary(actuary);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setAmountFilled(10);
        order.setApprovingActuary(supervisor);
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderDto actualOrderToOrderDtoResult = this.orderMapper.orderToOrderDto(order);
        assertNull(actualOrderToOrderDtoResult.getActionType());
        assertEquals(123L, actualOrderToOrderDtoResult.getUserId().longValue());
        assertTrue(actualOrderToOrderDtoResult.getTransactions().isEmpty());
        BigDecimal stopPrice = actualOrderToOrderDtoResult.getStopPrice();
        assertEquals(valueOfResult, stopPrice);
        assertEquals(SecurityType.STOCKS, actualOrderToOrderDtoResult.getSecurityType());
        assertTrue(actualOrderToOrderDtoResult.getAllOrNone());
        assertNull(actualOrderToOrderDtoResult.getAmountFilled());
        assertSame(fromResult, actualOrderToOrderDtoResult.getModificationDate());
        assertEquals(123L, actualOrderToOrderDtoResult.getSecurityId().longValue());
        assertEquals(OrderState.APPROVED, actualOrderToOrderDtoResult.getOrderState());
        assertEquals(123L, actualOrderToOrderDtoResult.getOrderId().longValue());
        BigDecimal fee = actualOrderToOrderDtoResult.getFee();
        assertEquals(valueOfResult, fee);
        BigDecimal margin = actualOrderToOrderDtoResult.getMargin();
        assertEquals(valueOfResult, margin);
        assertEquals(10, actualOrderToOrderDtoResult.getAmount().intValue());
        BigDecimal limitPrice = actualOrderToOrderDtoResult.getLimitPrice();
        assertEquals(valueOfResult, limitPrice);
        assertEquals("42", limitPrice.toString());
        assertEquals("42", fee.toString());
        assertEquals("42", margin.toString());
        assertEquals("42", stopPrice.toString());
        verify(order).getOrderState();
        verify(order).getSecurityType();
        verify(order).getAllOrNone();
        verify(order).getAmount();
        verify(order).getOrderId();
        verify(order).getSecurityId();
        verify(order).getUserId();
        verify(order).getFee();
        verify(order).getLimitPrice();
        verify(order).getMargin();
        verify(order).getStopPrice();
        verify(order).getModificationDate();
        verify(order).getTransactions();
        verify(order).setActionType((ActionType) any());
        verify(order).setActuary((Actuary) any());
        verify(order).setAllOrNone((Boolean) any());
        verify(order).setAmount((Integer) any());
        verify(order).setAmountFilled((Integer) any());
        verify(order).setApprovingActuary((Supervisor) any());
        verify(order).setFee((BigDecimal) any());
        verify(order).setLimitPrice((BigDecimal) any());
        verify(order).setMargin((BigDecimal) any());
        verify(order).setModificationDate((Date) any());
        verify(order).setOrderId((Long) any());
        verify(order).setOrderState((OrderState) any());
        verify(order).setSecurityId((Long) any());
        verify(order).setSecurityType((SecurityType) any());
        verify(order).setStopPrice((BigDecimal) any());
        verify(order).setTransactions((Set<Long>) any());
        verify(order).setUserId((Long) any());
    }

    /**
     * Method under test: {@link OrderMapper#ordersToOrderDtos(List)}
     */
    @Test
    void testOrdersToOrderDtos() {
        assertTrue(this.orderMapper.ordersToOrderDtos(new ArrayList<>()).isEmpty());
    }

    /**
     * Method under test: {@link OrderMapper#ordersToOrderDtos(List)}
     */
    @Test
    void testOrdersToOrderDtos2() {
        Actuary actuary = new Actuary();
        actuary.setActive(true);
        actuary.setActuaryType(ActuaryType.SUPERVISOR);
        actuary.setApprovalRequired(true);
        actuary.setId(123L);
        actuary.setOrders(new ArrayList<>());
        actuary.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary.setUsedLimit(BigDecimal.valueOf(42L));
        actuary.setUserId(123L);
        actuary.setVersion(1);

        Supervisor supervisor = new Supervisor();
        supervisor.setActive(true);
        supervisor.setActuaryType(ActuaryType.SUPERVISOR);
        supervisor.setApprovalRequired(true);
        supervisor.setId(123L);
        supervisor.setOrders(new ArrayList<>());
        supervisor.setSpendingLimit(BigDecimal.valueOf(42L));
        supervisor.setUsedLimit(BigDecimal.valueOf(42L));
        supervisor.setUserId(123L);
        supervisor.setVersion(1);

        Order order = new Order();
        order.setActionType(ActionType.BUY);
        order.setActuary(actuary);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setAmountFilled(10);
        order.setApprovingActuary(supervisor);
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order);
        assertEquals(1, this.orderMapper.ordersToOrderDtos(orderList).size());
    }

    /**
     * Method under test: {@link OrderMapper#ordersToOrderDtos(List)}
     */
    @Test
    void testOrdersToOrderDtos3() {
        Actuary actuary = new Actuary();
        actuary.setActive(true);
        actuary.setActuaryType(ActuaryType.SUPERVISOR);
        actuary.setApprovalRequired(true);
        actuary.setId(123L);
        actuary.setOrders(new ArrayList<>());
        actuary.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary.setUsedLimit(BigDecimal.valueOf(42L));
        actuary.setUserId(123L);
        actuary.setVersion(1);

        Supervisor supervisor = new Supervisor();
        supervisor.setActive(true);
        supervisor.setActuaryType(ActuaryType.SUPERVISOR);
        supervisor.setApprovalRequired(true);
        supervisor.setId(123L);
        supervisor.setOrders(new ArrayList<>());
        supervisor.setSpendingLimit(BigDecimal.valueOf(42L));
        supervisor.setUsedLimit(BigDecimal.valueOf(42L));
        supervisor.setUserId(123L);
        supervisor.setVersion(1);

        Order order = new Order();
        order.setActionType(ActionType.BUY);
        order.setActuary(actuary);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setAmountFilled(10);
        order.setApprovingActuary(supervisor);
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);

        Actuary actuary1 = new Actuary();
        actuary1.setActive(true);
        actuary1.setActuaryType(ActuaryType.SUPERVISOR);
        actuary1.setApprovalRequired(true);
        actuary1.setId(123L);
        actuary1.setOrders(new ArrayList<>());
        actuary1.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary1.setUsedLimit(BigDecimal.valueOf(42L));
        actuary1.setUserId(123L);
        actuary1.setVersion(1);

        Supervisor supervisor1 = new Supervisor();
        supervisor1.setActive(true);
        supervisor1.setActuaryType(ActuaryType.SUPERVISOR);
        supervisor1.setApprovalRequired(true);
        supervisor1.setId(123L);
        supervisor1.setOrders(new ArrayList<>());
        supervisor1.setSpendingLimit(BigDecimal.valueOf(42L));
        supervisor1.setUsedLimit(BigDecimal.valueOf(42L));
        supervisor1.setUserId(123L);
        supervisor1.setVersion(1);

        Order order1 = new Order();
        order1.setActionType(ActionType.BUY);
        order1.setActuary(actuary1);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setAmountFilled(10);
        order1.setApprovingActuary(supervisor1);
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        order1.setModificationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        order1.setOrderId(123L);
        order1.setOrderState(OrderState.APPROVED);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);

        ArrayList<Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order);
        assertEquals(2, this.orderMapper.ordersToOrderDtos(orderList).size());
    }
}

