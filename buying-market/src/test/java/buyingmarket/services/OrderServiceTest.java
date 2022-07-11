package buyingmarket.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buyingmarket.exceptions.OrderNotFoundException;
import buyingmarket.exceptions.UpdateNotAllowedException;
import buyingmarket.mappers.ActuaryMapper;
import buyingmarket.mappers.OrderMapper;
import buyingmarket.model.ActionType;
import buyingmarket.model.Actuary;
import buyingmarket.model.ActuaryType;
import buyingmarket.model.Order;
import buyingmarket.model.OrderState;
import buyingmarket.model.SecurityType;
import buyingmarket.model.Supervisor;
import buyingmarket.model.dto.OrderCreateDto;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.repositories.ActuaryRepository;
import buyingmarket.repositories.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
class OrderServiceTest {
    @MockBean
    private ActuaryService actuaryService;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OrderService.ExecuteOrderTask#ExecuteOrderTask(OrderService, Order, Boolean, String)}
     *   <li>{@link OrderService.ExecuteOrderTask#getOrder()}
     * </ul>
     */
    @Test
    void testExecuteOrderTaskConstructor() {
        OrderService orderService = new OrderService();

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
        assertSame(order, (orderService.new ExecuteOrderTask(order, true, "Jws")).getOrder());
    }

    /**
     * Method under test: default or parameterless constructor of {@link ExecuteOrderTask}
     */
    @Test
    void testExecuteOrderTaskConstructor2() {
        OrderService orderService = new OrderService();

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
        assertSame(order, (orderService.new ExecuteOrderTask(order, true, "Jws")).getOrder());
    }

    /**
     * Method under test: {@link ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun4() {
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
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
        ActuaryRepository actuaryRepository = mock(ActuaryRepository.class);
        ActuaryService actuaryService = new ActuaryService(actuaryRepository, new ActuaryMapper());

        OrderService orderService = new OrderService(actuaryService, orderRepository, new OrderMapper());

        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date modificationDate = Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant());
        Supervisor approvingActuary = new Supervisor();
        Actuary actuary1 = new Actuary();

        Order order1 = new Order(123L, 123L, 123L, 10, 10, SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee,
                OrderState.APPROVED, ActionType.BUY, modificationDate, approvingActuary, actuary1, new HashSet<>());
        order1.setAmount(1);
        (orderService.new ExecuteOrderTask(order1, true, "Jws")).run();
        verify(orderRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun5() {
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
        order.setOrderState(OrderState.DECLINED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
        ActuaryRepository actuaryRepository = mock(ActuaryRepository.class);
        ActuaryService actuaryService = new ActuaryService(actuaryRepository, new ActuaryMapper());

        OrderService orderService = new OrderService(actuaryService, orderRepository, new OrderMapper());

        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date modificationDate = Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant());
        Supervisor approvingActuary = new Supervisor();
        Actuary actuary1 = new Actuary();

        Order order1 = new Order(123L, 123L, 123L, 10, 10, SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee,
                OrderState.APPROVED, ActionType.BUY, modificationDate, approvingActuary, actuary1, new HashSet<>());
        order1.setAmount(1);
        (orderService.new ExecuteOrderTask(order1, true, "Jws")).run();
        verify(orderRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun6() {
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
        order.setOrderState(OrderState.DECLINED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
        ActuaryRepository actuaryRepository = mock(ActuaryRepository.class);
        ActuaryService actuaryService = new ActuaryService(actuaryRepository, new ActuaryMapper());

        OrderService orderService = new OrderService(actuaryService, orderRepository, new OrderMapper());

        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date modificationDate = Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant());
        Supervisor approvingActuary = new Supervisor();
        Actuary actuary1 = new Actuary();

        Order order1 = new Order(123L, 123L, 123L, 10, 10, SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee,
                OrderState.APPROVED, ActionType.BUY, modificationDate, approvingActuary, actuary1, new HashSet<>());
        order1.setAmount(1);
        (orderService.new ExecuteOrderTask(order1, true, "Jws")).run();
        verify(orderRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun7() {
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
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
        ActuaryRepository actuaryRepository = mock(ActuaryRepository.class);
        ActuaryService actuaryService = new ActuaryService(actuaryRepository, new ActuaryMapper());

        OrderService orderService = new OrderService(actuaryService, orderRepository, new OrderMapper());

        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date modificationDate = Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant());
        Supervisor approvingActuary = new Supervisor();
        Actuary actuary1 = new Actuary();

        Order order1 = new Order(123L, 123L, 123L, 10, 9, SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee,
                OrderState.APPROVED, ActionType.BUY, modificationDate, approvingActuary, actuary1, new HashSet<>());
        order1.setAmount(1);
        (orderService.new ExecuteOrderTask(order1, true, "Jws")).run();
        verify(orderRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link OrderService#validateOrder(Long, OrderState, String)}
     */
    @Test
    void testValidateOrder() {
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
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findById((Long) any())).thenReturn(ofResult);
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class,
                () -> this.orderService.validateOrder(123L, OrderState.APPROVED, "Jws"));
        verify(this.orderRepository).findById((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#validateOrder(Long, OrderState, String)}
     */
    @Test
    void testValidateOrder2() {
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
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findById((Long) any())).thenReturn(ofResult);
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class,
                () -> this.orderService.validateOrder(123L, OrderState.APPROVED, "Jws"));
        verify(this.orderRepository).findById((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderCreateDto, String)}
     */
    @Test
    void testCreateOrder() {
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
        when(this.orderMapper.orderCreateDtoToOrder((OrderCreateDto) any())).thenReturn(order);
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setActionType(ActionType.BUY);
        orderCreateDto.setAllOrNone(true);
        orderCreateDto.setAmount(10);
        orderCreateDto.setLimitPrice(BigDecimal.valueOf(42L));
        orderCreateDto.setMargin(BigDecimal.valueOf(42L));
        orderCreateDto.setSecurityId(123L);
        orderCreateDto.setSecurityType(SecurityType.STOCKS);
        orderCreateDto.setStopPrice(BigDecimal.valueOf(42L));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.createOrder(orderCreateDto, "Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderCreateDto, String)}
     */
    @Test
    void testCreateOrder3() {
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
        when(this.orderMapper.orderCreateDtoToOrder((OrderCreateDto) any())).thenReturn(order);
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.createOrder(new OrderCreateDto(), "Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderCreateDto, String)}
     */
    @Test
    void testCreateOrder4() {
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
        Order order = mock(Order.class);
        when(order.getSecurityId()).thenThrow(new UpdateNotAllowedException("An error occurred"));
        when(order.getSecurityType()).thenReturn(SecurityType.STOCKS);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        when(this.orderMapper.orderCreateDtoToOrder((OrderCreateDto) any())).thenReturn(order);

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setActionType(ActionType.BUY);
        orderCreateDto.setAllOrNone(true);
        orderCreateDto.setAmount(10);
        orderCreateDto.setLimitPrice(BigDecimal.valueOf(42L));
        orderCreateDto.setMargin(BigDecimal.valueOf(42L));
        orderCreateDto.setSecurityId(123L);
        orderCreateDto.setSecurityType(SecurityType.STOCKS);
        orderCreateDto.setStopPrice(BigDecimal.valueOf(42L));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.createOrder(orderCreateDto, "Jws"));
        verify(this.orderMapper).orderCreateDtoToOrder((OrderCreateDto) any());
        verify(order).getSecurityType();
        verify(order).getSecurityId();
        verify(order).setActionType((ActionType) any());
        verify(order, atLeast(1)).setActuary((Actuary) any());
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
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderCreateDto, String)}
     */
    @Test
    void testCreateOrder6() {
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
        Order order = mock(Order.class);
        when(order.getSecurityId()).thenThrow(new UpdateNotAllowedException("An error occurred"));
        when(order.getSecurityType()).thenReturn(SecurityType.STOCKS);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        when(this.orderMapper.orderCreateDtoToOrder((OrderCreateDto) any())).thenReturn(order);

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.createOrder(new OrderCreateDto(), "Jws"));
        verify(this.orderMapper).orderCreateDtoToOrder((OrderCreateDto) any());
        verify(order).getSecurityType();
        verify(order).getSecurityId();
        verify(order).setActionType((ActionType) any());
        verify(order, atLeast(1)).setActuary((Actuary) any());
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
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findAllOrdersForUser(String)}
     */
    @Test
    void testFindAllOrdersForUser() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        ArrayList<OrderDto> orderDtoList = new ArrayList<>();
        when(this.orderMapper.ordersToOrderDtos((List<Order>) any())).thenReturn(orderDtoList);

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary);
        List<OrderDto> actualFindAllOrdersForUserResult = this.orderService.findAllOrdersForUser("Jws");
        assertSame(orderDtoList, actualFindAllOrdersForUserResult);
        assertTrue(actualFindAllOrdersForUserResult.isEmpty());
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderMapper).ordersToOrderDtos((List<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findAllOrdersForUser(String)}
     */
    @Test
    void testFindAllOrdersForUser2() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        when(this.orderMapper.ordersToOrderDtos((List<Order>) any())).thenReturn(new ArrayList<>());
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.findAllOrdersForUser("Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findAllOrdersForUser(String)}
     */
    @Test
    void testFindAllOrdersForUser3() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        ArrayList<OrderDto> orderDtoList = new ArrayList<>();
        when(this.orderMapper.ordersToOrderDtos((List<Order>) any())).thenReturn(orderDtoList);

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary);
        List<OrderDto> actualFindAllOrdersForUserResult = this.orderService.findAllOrdersForUser("Jws");
        assertSame(orderDtoList, actualFindAllOrdersForUserResult);
        assertTrue(actualFindAllOrdersForUserResult.isEmpty());
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderMapper).ordersToOrderDtos((List<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findAllOrdersForUser(String)}
     */
    @Test
    void testFindAllOrdersForUser4() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        when(this.orderMapper.ordersToOrderDtos((List<Order>) any())).thenReturn(new ArrayList<>());
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.findAllOrdersForUser("Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    void testFindOrderForUser() {
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
        Optional<Order> ofResult = Optional.of(order);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);
        OrderDto orderDto = new OrderDto();
        when(this.orderMapper.orderToOrderDto((Order) any())).thenReturn(orderDto);

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);
        assertSame(orderDto, this.orderService.findOrderForUser(123L, "Jws"));
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(this.orderMapper).orderToOrderDto((Order) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    void testFindOrderForUser2() {
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
        Optional<Order> ofResult = Optional.of(order);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);
        when(this.orderMapper.orderToOrderDto((Order) any())).thenReturn(new OrderDto());
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.findOrderForUser(123L, "Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    void testFindOrderForUser3() {
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(Optional.empty());
        when(this.orderMapper.orderToOrderDto((Order) any())).thenReturn(new OrderDto());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary);
        assertThrows(OrderNotFoundException.class, () -> this.orderService.findOrderForUser(123L, "Jws"));
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    void testFindOrderForUser4() {
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
        Optional<Order> ofResult = Optional.of(order);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);
        OrderDto orderDto = new OrderDto();
        when(this.orderMapper.orderToOrderDto((Order) any())).thenReturn(orderDto);

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);
        assertSame(orderDto, this.orderService.findOrderForUser(123L, "Jws"));
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(this.orderMapper).orderToOrderDto((Order) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    void testFindOrderForUser5() {
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
        Optional<Order> ofResult = Optional.of(order);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);
        when(this.orderMapper.orderToOrderDto((Order) any())).thenReturn(new OrderDto());
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.findOrderForUser(123L, "Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    void testFindOrderForUser6() {
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(Optional.empty());
        when(this.orderMapper.orderToOrderDto((Order) any())).thenReturn(new OrderDto());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary);
        assertThrows(OrderNotFoundException.class, () -> this.orderService.findOrderForUser(123L, "Jws"));
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteOrder(Long, String)}
     */
    @Test
    void testDeleteOrder() {
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
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);

        Actuary actuary2 = new Actuary();
        actuary2.setActive(true);
        actuary2.setActuaryType(ActuaryType.SUPERVISOR);
        actuary2.setApprovalRequired(true);
        actuary2.setId(123L);
        actuary2.setOrders(new ArrayList<>());
        actuary2.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary2.setUsedLimit(BigDecimal.valueOf(42L));
        actuary2.setUserId(123L);
        actuary2.setVersion(1);
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary2);
        this.orderService.deleteOrder(123L, "Jws");
        verify(this.orderRepository).save((Order) any());
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteOrder(Long, String)}
     */
    @Test
    void testDeleteOrder2() {
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
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.deleteOrder(123L, "Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteOrder(Long, String)}
     */
    @Test
    void testDeleteOrder3() {
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
        Order order = mock(Order.class);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);

        Actuary actuary2 = new Actuary();
        actuary2.setActive(true);
        actuary2.setActuaryType(ActuaryType.SUPERVISOR);
        actuary2.setApprovalRequired(true);
        actuary2.setId(123L);
        actuary2.setOrders(new ArrayList<>());
        actuary2.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary2.setUsedLimit(BigDecimal.valueOf(42L));
        actuary2.setUserId(123L);
        actuary2.setVersion(1);
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary2);
        this.orderService.deleteOrder(123L, "Jws");
        verify(this.orderRepository).save((Order) any());
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(order).setActionType((ActionType) any());
        verify(order).setActuary((Actuary) any());
        verify(order).setAllOrNone((Boolean) any());
        verify(order).setAmount((Integer) any());
        verify(order).setAmountFilled((Integer) any());
        verify(order).setApprovingActuary((Supervisor) any());
        verify(order).setFee((BigDecimal) any());
        verify(order).setLimitPrice((BigDecimal) any());
        verify(order).setMargin((BigDecimal) any());
        verify(order, atLeast(1)).setModificationDate((Date) any());
        verify(order).setOrderId((Long) any());
        verify(order, atLeast(1)).setOrderState((OrderState) any());
        verify(order).setSecurityId((Long) any());
        verify(order).setSecurityType((SecurityType) any());
        verify(order).setStopPrice((BigDecimal) any());
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteOrder(Long, String)}
     */
    @Test
    void testDeleteOrder4() {
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
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);

        Actuary actuary2 = new Actuary();
        actuary2.setActive(true);
        actuary2.setActuaryType(ActuaryType.SUPERVISOR);
        actuary2.setApprovalRequired(true);
        actuary2.setId(123L);
        actuary2.setOrders(new ArrayList<>());
        actuary2.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary2.setUsedLimit(BigDecimal.valueOf(42L));
        actuary2.setUserId(123L);
        actuary2.setVersion(1);
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary2);
        this.orderService.deleteOrder(123L, "Jws");
        verify(this.orderRepository).save((Order) any());
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteOrder(Long, String)}
     */
    @Test
    void testDeleteOrder5() {
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
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.deleteOrder(123L, "Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteOrder(Long, String)}
     */
    @Test
    void testDeleteOrder6() {
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
        Order order = mock(Order.class);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        Optional<Order> ofResult = Optional.of(order);

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
        when(this.orderRepository.save((Order) any())).thenReturn(order1);
        when(this.orderRepository.findByOrderIdAndActuary((Long) any(), (Actuary) any())).thenReturn(ofResult);

        Actuary actuary2 = new Actuary();
        actuary2.setActive(true);
        actuary2.setActuaryType(ActuaryType.SUPERVISOR);
        actuary2.setApprovalRequired(true);
        actuary2.setId(123L);
        actuary2.setOrders(new ArrayList<>());
        actuary2.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary2.setUsedLimit(BigDecimal.valueOf(42L));
        actuary2.setUserId(123L);
        actuary2.setVersion(1);
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary2);
        this.orderService.deleteOrder(123L, "Jws");
        verify(this.orderRepository).save((Order) any());
        verify(this.orderRepository).findByOrderIdAndActuary((Long) any(), (Actuary) any());
        verify(order).setActionType((ActionType) any());
        verify(order).setActuary((Actuary) any());
        verify(order).setAllOrNone((Boolean) any());
        verify(order).setAmount((Integer) any());
        verify(order).setAmountFilled((Integer) any());
        verify(order).setApprovingActuary((Supervisor) any());
        verify(order).setFee((BigDecimal) any());
        verify(order).setLimitPrice((BigDecimal) any());
        verify(order).setMargin((BigDecimal) any());
        verify(order, atLeast(1)).setModificationDate((Date) any());
        verify(order).setOrderId((Long) any());
        verify(order, atLeast(1)).setOrderState((OrderState) any());
        verify(order).setSecurityId((Long) any());
        verify(order).setSecurityType((SecurityType) any());
        verify(order).setStopPrice((BigDecimal) any());
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser2() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.deleteAllOrdersForUser("Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser3() {
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
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(orderList);
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser4() {
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
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(orderList);
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

        Actuary actuary2 = new Actuary();
        actuary2.setActive(true);
        actuary2.setActuaryType(ActuaryType.SUPERVISOR);
        actuary2.setApprovalRequired(true);
        actuary2.setId(123L);
        actuary2.setOrders(new ArrayList<>());
        actuary2.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary2.setUsedLimit(BigDecimal.valueOf(42L));
        actuary2.setUserId(123L);
        actuary2.setVersion(1);
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary2);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser5() {
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
        Order order = mock(Order.class);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(orderList);
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(order).setActionType((ActionType) any());
        verify(order).setActuary((Actuary) any());
        verify(order).setAllOrNone((Boolean) any());
        verify(order).setAmount((Integer) any());
        verify(order).setAmountFilled((Integer) any());
        verify(order).setApprovingActuary((Supervisor) any());
        verify(order).setFee((BigDecimal) any());
        verify(order).setLimitPrice((BigDecimal) any());
        verify(order).setMargin((BigDecimal) any());
        verify(order, atLeast(1)).setModificationDate((Date) any());
        verify(order).setOrderId((Long) any());
        verify(order, atLeast(1)).setOrderState((OrderState) any());
        verify(order).setSecurityId((Long) any());
        verify(order).setSecurityType((SecurityType) any());
        verify(order).setStopPrice((BigDecimal) any());
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser6() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser7() {
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(new ArrayList<>());
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());
        when(this.actuaryService.getActuary((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.deleteAllOrdersForUser("Jws"));
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser8() {
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
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(orderList);
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser9() {
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
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(orderList);
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

        Actuary actuary2 = new Actuary();
        actuary2.setActive(true);
        actuary2.setActuaryType(ActuaryType.SUPERVISOR);
        actuary2.setApprovalRequired(true);
        actuary2.setId(123L);
        actuary2.setOrders(new ArrayList<>());
        actuary2.setSpendingLimit(BigDecimal.valueOf(42L));
        actuary2.setUsedLimit(BigDecimal.valueOf(42L));
        actuary2.setUserId(123L);
        actuary2.setVersion(1);
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary2);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#deleteAllOrdersForUser(String)}
     */
    @Test
    void testDeleteAllOrdersForUser10() {
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
        Order order = mock(Order.class);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        when(this.orderRepository.findAllByActuary((Actuary) any())).thenReturn(orderList);
        when(this.orderRepository.saveAll((Iterable<Order>) any())).thenReturn(new ArrayList<>());

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
        when(this.actuaryService.getActuary((String) any())).thenReturn(actuary1);
        this.orderService.deleteAllOrdersForUser("Jws");
        verify(this.orderRepository).findAllByActuary((Actuary) any());
        verify(this.orderRepository).saveAll((Iterable<Order>) any());
        verify(order).setActionType((ActionType) any());
        verify(order).setActuary((Actuary) any());
        verify(order).setAllOrNone((Boolean) any());
        verify(order).setAmount((Integer) any());
        verify(order).setAmountFilled((Integer) any());
        verify(order).setApprovingActuary((Supervisor) any());
        verify(order).setFee((BigDecimal) any());
        verify(order).setLimitPrice((BigDecimal) any());
        verify(order).setMargin((BigDecimal) any());
        verify(order, atLeast(1)).setModificationDate((Date) any());
        verify(order).setOrderId((Long) any());
        verify(order, atLeast(1)).setOrderState((OrderState) any());
        verify(order).setSecurityId((Long) any());
        verify(order).setSecurityType((SecurityType) any());
        verify(order).setStopPrice((BigDecimal) any());
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
        verify(this.actuaryService).getActuary((String) any());
    }

    /**
     * Method under test: {@link OrderService#getSecurityFromOrder(Order)}
     */
    @Test
    void testGetSecurityFromOrder() {
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
        Order order = mock(Order.class);
        when(order.getSecurityId()).thenThrow(new UpdateNotAllowedException("An error occurred"));
        when(order.getSecurityType()).thenReturn(SecurityType.STOCKS);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.getSecurityFromOrder(order));
        verify(order).getSecurityType();
        verify(order).getSecurityId();
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
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
    }

    /**
     * Method under test: {@link OrderService#getSecurityFromOrder(Order)}
     */
    @Test
    void testGetSecurityFromOrder4() {
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
        Order order = mock(Order.class);
        when(order.getSecurityId()).thenThrow(new UpdateNotAllowedException("An error occurred"));
        when(order.getSecurityType()).thenReturn(SecurityType.STOCKS);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.getSecurityFromOrder(order));
        verify(order).getSecurityType();
        verify(order).getSecurityId();
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
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
    }

    /**
     * Method under test: {@link OrderService#createTransaction(String, Order, String, int, int, int, int)}
     */
    @Test
    void testCreateTransaction2() {
        when(this.orderMapper.orderToOrderDto((Order) any())).thenReturn(new OrderDto());
        when(this.actuaryService.getUserId((String) any())).thenThrow(new UpdateNotAllowedException("An error occurred"));

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
        assertThrows(UpdateNotAllowedException.class,
                () -> this.orderService.createTransaction("Jwt", order, "Text", 1, 1, 1, 1));
        verify(this.orderMapper).orderToOrderDto((Order) any());
        verify(this.actuaryService).getUserId((String) any());
    }

    /**
     * Method under test: {@link OrderService#execute(Order, String)}
     */
    @Test
    void testExecute() {
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
        Order order = mock(Order.class);
        when(order.getSecurityId()).thenThrow(new UpdateNotAllowedException("An error occurred"));
        when(order.getSecurityType()).thenReturn(SecurityType.STOCKS);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.execute(order, "Jws"));
        verify(order).getSecurityType();
        verify(order).getSecurityId();
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
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
    }

    /**
     * Method under test: {@link OrderService#execute(Order, String)}
     */
    @Test
    void testExecute4() {
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
        Order order = mock(Order.class);
        when(order.getSecurityId()).thenThrow(new UpdateNotAllowedException("An error occurred"));
        when(order.getSecurityType()).thenReturn(SecurityType.STOCKS);
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
        doNothing().when(order).setTransactions((java.util.Set<Long>) any());
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
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        order.setModificationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        order.setOrderId(123L);
        order.setOrderState(OrderState.APPROVED);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        assertThrows(UpdateNotAllowedException.class, () -> this.orderService.execute(order, "Jws"));
        verify(order).getSecurityType();
        verify(order).getSecurityId();
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
        verify(order).setTransactions((java.util.Set<Long>) any());
        verify(order).setUserId((Long) any());
    }
}

