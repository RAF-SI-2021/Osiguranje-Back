package buyingmarket.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import buyingmarket.exceptions.OrderNotFoundException;
import buyingmarket.formulas.FormulaCalculator;
import buyingmarket.mappers.OrderMapper;
import buyingmarket.mappers.TransactionMapper;
import buyingmarket.model.Order;
import buyingmarket.model.SecurityType;
import buyingmarket.model.Transaction;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.repositories.OrderRepository;
import buyingmarket.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
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
    private FormulaCalculator formulaCalculator;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @MockBean
    private TransactionService transactionService;

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask
     */
    @Test
    void testExecuteOrderTaskConstructor() {

        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        orderService.new ExecuteOrderTask(10, order, 1L);

    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask
     */
    @Test
    void testExecuteOrderTaskConstructor2() {

        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        orderService.new ExecuteOrderTask(10, order, 1L);

    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OrderService.ExecuteOrderTask#getAmount()}
     *   <li>{@link OrderService.ExecuteOrderTask#getOrder()}
     *   <li>{@link OrderService.ExecuteOrderTask#getVolume()}
     * </ul>
     */
    @Test
    void testExecuteOrderTaskConstructor3() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderService.ExecuteOrderTask actualExecuteOrderTask = orderService.new ExecuteOrderTask(10, order, 1L);

        assertEquals(10, actualExecuteOrderTask.getAmount());
        assertSame(order, actualExecuteOrderTask.getOrder());
        assertEquals(1L, actualExecuteOrderTask.getVolume().longValue());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OrderService.ExecuteOrderTask#getAmount()}
     *   <li>{@link OrderService.ExecuteOrderTask#getOrder()}
     *   <li>{@link OrderService.ExecuteOrderTask#getVolume()}
     * </ul>
     */
    @Test
    void testExecuteOrderTaskConstructor4() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderService.ExecuteOrderTask actualExecuteOrderTask = orderService.new ExecuteOrderTask(10, order, 1L);

        assertEquals(10, actualExecuteOrderTask.getAmount());
        assertSame(order, actualExecuteOrderTask.getOrder());
        assertEquals(1L, actualExecuteOrderTask.getVolume().longValue());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OrderService.ExecuteOrderTask#getAmount()}
     *   <li>{@link OrderService.ExecuteOrderTask#getOrder()}
     *   <li>{@link OrderService.ExecuteOrderTask#getVolume()}
     * </ul>
     */
    @Test
    void testExecuteOrderTaskConstructor5() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderService.ExecuteOrderTask actualExecuteOrderTask = orderService.new ExecuteOrderTask(10, order, 1L);

        assertEquals(10, actualExecuteOrderTask.getAmount());
        assertSame(order, actualExecuteOrderTask.getOrder());
        assertEquals(1L, actualExecuteOrderTask.getVolume().longValue());
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
     */
    @Test
    @Disabled("JWS")
    void testCreateOrder() {
        this.orderService.createOrder(new OrderDto(), "Jws");
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
     */
    @Test
    @Disabled("JWS")
    void testCreateOrder2() {
        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        BigDecimal cost = BigDecimal.valueOf(42L);
        this.orderService.createOrder(new OrderDto(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice,
                stopPrice, fee, cost, true, new HashSet<>()), "Jws");
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
     */
    @Test
    @Disabled("JWS")
    void testCreateOrder4() {
        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        BigDecimal cost = BigDecimal.valueOf(42L);
        this.orderService.createOrder(new OrderDto(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice,
                stopPrice, fee, cost, true, new HashSet<>()), "Jws");
    }

    /**
     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
     */
    @Test
    @Disabled("JWS")
    void testCreateOrder6() {
        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        BigDecimal cost = BigDecimal.valueOf(42L);
        this.orderService.createOrder(new OrderDto(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice,
                stopPrice, fee, cost, true, new HashSet<>()), "Jws");
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    @Disabled("JWS")
    void testExecuteOrderTaskRun() {
        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);

        Transaction transaction = new Transaction();
        transaction.setId(123L);
        transaction.setOrder(order1);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(1L);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        TransactionService transactionService = new TransactionService(transactionRepository);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(), 1L)).run());
        verify(orderRepository).findById((Long) any());
        verify(transactionRepository).save((Transaction) any());
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun2() {

        Order order = new Order();
        order.setActive(false);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);

        Transaction transaction = new Transaction();
        transaction.setId(123L);
        transaction.setOrder(order1);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(1L);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        TransactionService transactionService = new TransactionService(transactionRepository);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        (orderService.new ExecuteOrderTask(10, new Order(), 1L)).run();
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun3() {

        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);

        Transaction transaction = new Transaction();
        transaction.setId(123L);
        transaction.setOrder(order1);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(10L);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        TransactionService transactionService = new TransactionService(transactionRepository);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        (orderService.new ExecuteOrderTask(10, order1, 100L)).run();
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun4() {
        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(2);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());

        Transaction transaction = new Transaction();
        transaction.setId(123L);

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);
        transaction.setOrder(order1);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(1L);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        TransactionService transactionService = new TransactionService(transactionRepository);
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(), 1L)).run());
        verify(orderRepository).findById((Long) any());
        verify(transactionRepository).save((Transaction) any());
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun5() {
        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);

        Transaction transaction = new Transaction();
        transaction.setId(123L);
        transaction.setOrder(order1);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(1L);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        TransactionService transactionService = new TransactionService(transactionRepository);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        BigDecimal cost = BigDecimal.valueOf(42L);
        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(123L, 123L, 123L, 10,
                SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee, cost, true, new HashSet<>()), 1L)).run());
        verify(orderRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    void testExecuteOrderTaskRun6() {
        Order order = new Order();
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());

        Transaction transaction = new Transaction();
        transaction.setId(123L);

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);
        transaction.setOrder(order1);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(1L);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        TransactionService transactionService = new TransactionService(transactionRepository);
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(), 0L)).run());
        verify(orderRepository).findById((Long) any());
        verify(transactionRepository).save((Transaction) any());
    }

    /**
     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
     */
    @Test
    @Disabled
    void testExecuteOrderTaskRun7() {
        BigDecimal margin = BigDecimal.valueOf(42L);
        BigDecimal limitPrice = BigDecimal.valueOf(42L);
        BigDecimal stopPrice = BigDecimal.valueOf(42L);
        BigDecimal fee = BigDecimal.valueOf(42L);
        BigDecimal cost = BigDecimal.valueOf(42L);

        Order order = new Order(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee, cost,
                true, new HashSet<>());
        order.setActive(true);
        order.setAllOrNone(true);
        order.setAmount(10);
        order.setCost(BigDecimal.valueOf(42L));
        order.setFee(BigDecimal.valueOf(42L));
        order.setLimitPrice(BigDecimal.valueOf(42L));
        order.setMargin(BigDecimal.valueOf(42L));
        order.setOrderId(123L);
        order.setSecurityId(123L);
        order.setSecurityType(SecurityType.STOCKS);
        order.setStopPrice(BigDecimal.valueOf(42L));
        order.setTransactions(new HashSet<>());
        order.setUserId(123L);
        OrderRepository orderRepository = mock(OrderRepository.class);
        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));

        Order order1 = new Order();
        order1.setActive(true);
        order1.setAllOrNone(true);
        order1.setAmount(10);
        order1.setCost(BigDecimal.valueOf(42L));
        order1.setFee(BigDecimal.valueOf(42L));
        order1.setLimitPrice(BigDecimal.valueOf(42L));
        order1.setMargin(BigDecimal.valueOf(42L));
        order1.setOrderId(123L);
        order1.setSecurityId(123L);
        order1.setSecurityType(SecurityType.STOCKS);
        order1.setStopPrice(BigDecimal.valueOf(42L));
        order1.setTransactions(new HashSet<>());
        order1.setUserId(123L);

        Transaction transaction = new Transaction();
        transaction.setId(123L);
        transaction.setOrder(order1);
        transaction.setPrice(BigDecimal.valueOf(42L));
        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setVolume(1L);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
        TransactionService transactionService = new TransactionService(transactionRepository);
        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
                new FormulaCalculator());

        (orderService.new ExecuteOrderTask(10, new Order(), 1000L)).run();
        verify(orderRepository).findById((Long) any());
        verify(transactionRepository).save((Transaction) any());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    @Disabled("JWS")
    void testFindOrderForUser() {

        OrderDto userOrder = this.orderService.findOrderForUser(123L, "xxxxxxx.yyyyyyyyy.zzzzz");
        assertEquals(123L, userOrder.getOrderId());
    }

    /**
     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
     */
    @Test
    @Disabled("JWS")
    void testFindOrderForUser2() {

        try{
            OrderDto userOrder = this.orderService.findOrderForUser(123L, "x.y.z");
        }catch (OrderNotFoundException e){
            assertEquals("No order with given id could be found for user", e.getMessage());
        }
    }
}

