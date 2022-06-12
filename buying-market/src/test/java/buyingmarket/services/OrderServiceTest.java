//package buyingmarket.services;
//
//import buyingmarket.exceptions.OrderNotFoundException;
//import buyingmarket.exceptions.UpdateNotAllowedException;
//import buyingmarket.formulas.FormulaCalculator;
//import buyingmarket.mappers.OrderMapper;
//import buyingmarket.mappers.TransactionMapper;
//import buyingmarket.model.Order;
//import buyingmarket.model.SecurityType;
//import buyingmarket.model.Transaction;
//import buyingmarket.model.dto.OrderDto;
//import buyingmarket.model.dto.SecurityDto;
//import buyingmarket.repositories.OrderRepository;
//import buyingmarket.repositories.TransactionRepository;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ContextConfiguration(classes = {OrderService.class})
//@ExtendWith(SpringExtension.class)
//class OrderServiceTest {
//    @MockBean
//    private FormulaCalculator formulaCalculator;
//
//    @MockBean
//    private OrderMapper orderMapper;
//
//    @MockBean
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderService orderService;
//
//    @MockBean
//    private TransactionService transactionService;
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask
//     */
//    @Test
//    void testExecuteOrderTaskConstructor() {
//
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        orderService.new ExecuteOrderTask(10, order, 1L);
//
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask
//     */
//    @Test
//    void testExecuteOrderTaskConstructor2() {
//
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        orderService.new ExecuteOrderTask(10, order, 1L);
//
//    }
//
//    /**
//     * Methods under test:
//     *
//     * <ul>
//     *   <li>{@link OrderService.ExecuteOrderTask#getAmount()}
//     *   <li>{@link OrderService.ExecuteOrderTask#getOrder()}
//     *   <li>{@link OrderService.ExecuteOrderTask#getVolume()}
//     * </ul>
//     */
//    @Test
//    void testExecuteOrderTaskConstructor3() {
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderService.ExecuteOrderTask actualExecuteOrderTask = orderService.new ExecuteOrderTask(10, order, 1L);
//
//        assertEquals(10, actualExecuteOrderTask.getAmount());
//        assertSame(order, actualExecuteOrderTask.getOrder());
//        assertEquals(1L, actualExecuteOrderTask.getVolume().longValue());
//    }
//
//    /**
//     * Methods under test:
//     *
//     * <ul>
//     *   <li>{@link OrderService.ExecuteOrderTask#getAmount()}
//     *   <li>{@link OrderService.ExecuteOrderTask#getOrder()}
//     *   <li>{@link OrderService.ExecuteOrderTask#getVolume()}
//     * </ul>
//     */
//    @Test
//    void testExecuteOrderTaskConstructor4() {
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderService.ExecuteOrderTask actualExecuteOrderTask = orderService.new ExecuteOrderTask(10, order, 1L);
//
//        assertEquals(10, actualExecuteOrderTask.getAmount());
//        assertSame(order, actualExecuteOrderTask.getOrder());
//        assertEquals(1L, actualExecuteOrderTask.getVolume().longValue());
//    }
//
//    /**
//     * Methods under test:
//     *
//     * <ul>
//     *   <li>{@link OrderService.ExecuteOrderTask#getAmount()}
//     *   <li>{@link OrderService.ExecuteOrderTask#getOrder()}
//     *   <li>{@link OrderService.ExecuteOrderTask#getVolume()}
//     * </ul>
//     */
//    @Test
//    void testExecuteOrderTaskConstructor5() {
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        TransactionService transactionService = new TransactionService(mock(TransactionRepository.class));
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderService.ExecuteOrderTask actualExecuteOrderTask = orderService.new ExecuteOrderTask(10, order, 1L);
//
//        assertEquals(10, actualExecuteOrderTask.getAmount());
//        assertSame(order, actualExecuteOrderTask.getOrder());
//        assertEquals(1L, actualExecuteOrderTask.getVolume().longValue());
//    }
//
//    /**
//     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
//     */
//    @Test
//    @Disabled("JWS")
//    void testCreateOrder() {
//        this.orderService.createOrder(new OrderDto(), "Jws");
//    }
//
//    /**
//     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
//     */
//    @Test
//    @Disabled("JWS")
//    void testCreateOrder2() {
//        BigDecimal margin = BigDecimal.valueOf(42L);
//        BigDecimal limitPrice = BigDecimal.valueOf(42L);
//        BigDecimal stopPrice = BigDecimal.valueOf(42L);
//        BigDecimal fee = BigDecimal.valueOf(42L);
//        BigDecimal cost = BigDecimal.valueOf(42L);
//        this.orderService.createOrder(new OrderDto(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice,
//                stopPrice, fee, cost, true, new HashSet<>()), "Jws");
//    }
//
//    /**
//     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
//     */
//    @Test
//    @Disabled("JWS")
//    void testCreateOrder4() {
//        BigDecimal margin = BigDecimal.valueOf(42L);
//        BigDecimal limitPrice = BigDecimal.valueOf(42L);
//        BigDecimal stopPrice = BigDecimal.valueOf(42L);
//        BigDecimal fee = BigDecimal.valueOf(42L);
//        BigDecimal cost = BigDecimal.valueOf(42L);
//        this.orderService.createOrder(new OrderDto(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice,
//                stopPrice, fee, cost, true, new HashSet<>()), "Jws");
//    }
//
//    /**
//     * Method under test: {@link OrderService#createOrder(OrderDto, String)}
//     */
//    @Test
//    @Disabled("JWS")
//    void testCreateOrder6() {
//        BigDecimal margin = BigDecimal.valueOf(42L);
//        BigDecimal limitPrice = BigDecimal.valueOf(42L);
//        BigDecimal stopPrice = BigDecimal.valueOf(42L);
//        BigDecimal fee = BigDecimal.valueOf(42L);
//        BigDecimal cost = BigDecimal.valueOf(42L);
//        this.orderService.createOrder(new OrderDto(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice,
//                stopPrice, fee, cost, true, new HashSet<>()), "Jws");
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
//     */
//    @Test
//    @Disabled("JWS")
//    void testExecuteOrderTaskRun() {
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
//
//        Order order1 = new Order();
//        order1.setActive(true);
//        order1.setAllOrNone(true);
//        order1.setAmount(10);
//        order1.setCost(BigDecimal.valueOf(42L));
//        order1.setFee(BigDecimal.valueOf(42L));
//        order1.setLimitPrice(BigDecimal.valueOf(42L));
//        order1.setMargin(BigDecimal.valueOf(42L));
//        order1.setOrderId(123L);
//        order1.setSecurityId(123L);
//        order1.setSecurityType(SecurityType.STOCKS);
//        order1.setStopPrice(BigDecimal.valueOf(42L));
//        order1.setTransactions(new HashSet<>());
//        order1.setUserId(123L);
//
//        Transaction transaction = new Transaction();
//        transaction.setId(123L);
//        transaction.setOrder(order1);
//        transaction.setPrice(BigDecimal.valueOf(42L));
//        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
//        transaction.setVolume(1L);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
//        TransactionService transactionService = new TransactionService(transactionRepository);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(), 1L)).run());
//        verify(orderRepository).findById((Long) any());
//        verify(transactionRepository).save((Transaction) any());
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
//     */
//    @Test
//    void testExecuteOrderTaskRun2() {
//
//        Order order = new Order();
//        order.setActive(false);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
//
//        Order order1 = new Order();
//        order1.setActive(true);
//        order1.setAllOrNone(true);
//        order1.setAmount(10);
//        order1.setCost(BigDecimal.valueOf(42L));
//        order1.setFee(BigDecimal.valueOf(42L));
//        order1.setLimitPrice(BigDecimal.valueOf(42L));
//        order1.setMargin(BigDecimal.valueOf(42L));
//        order1.setOrderId(123L);
//        order1.setSecurityId(123L);
//        order1.setSecurityType(SecurityType.STOCKS);
//        order1.setStopPrice(BigDecimal.valueOf(42L));
//        order1.setTransactions(new HashSet<>());
//        order1.setUserId(123L);
//
//        Transaction transaction = new Transaction();
//        transaction.setId(123L);
//        transaction.setOrder(order1);
//        transaction.setPrice(BigDecimal.valueOf(42L));
//        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
//        transaction.setVolume(1L);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
//        TransactionService transactionService = new TransactionService(transactionRepository);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        (orderService.new ExecuteOrderTask(10, new Order(), 1L)).run();
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
//     */
//    @Test
//    void testExecuteOrderTaskRun3() {
//
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
//
//        Order order1 = new Order();
//        order1.setActive(true);
//        order1.setAllOrNone(true);
//        order1.setAmount(10);
//        order1.setCost(BigDecimal.valueOf(42L));
//        order1.setFee(BigDecimal.valueOf(42L));
//        order1.setLimitPrice(BigDecimal.valueOf(42L));
//        order1.setMargin(BigDecimal.valueOf(42L));
//        order1.setOrderId(123L);
//        order1.setSecurityId(123L);
//        order1.setSecurityType(SecurityType.STOCKS);
//        order1.setStopPrice(BigDecimal.valueOf(42L));
//        order1.setTransactions(new HashSet<>());
//        order1.setUserId(123L);
//
//        Transaction transaction = new Transaction();
//        transaction.setId(123L);
//        transaction.setOrder(order1);
//        transaction.setPrice(BigDecimal.valueOf(42L));
//        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
//        transaction.setVolume(10L);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
//        TransactionService transactionService = new TransactionService(transactionRepository);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        (orderService.new ExecuteOrderTask(10, order1, 100L)).run();
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
//     */
//    @Test
//    @Disabled
//    void testExecuteOrderTaskRun4() {
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(2);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//
//        Transaction transaction = new Transaction();
//        transaction.setId(123L);
//
//        Order order1 = new Order();
//        order1.setActive(true);
//        order1.setAllOrNone(true);
//        order1.setAmount(10);
//        order1.setCost(BigDecimal.valueOf(42L));
//        order1.setFee(BigDecimal.valueOf(42L));
//        order1.setLimitPrice(BigDecimal.valueOf(42L));
//        order1.setMargin(BigDecimal.valueOf(42L));
//        order1.setOrderId(123L);
//        order1.setSecurityId(123L);
//        order1.setSecurityType(SecurityType.STOCKS);
//        order1.setStopPrice(BigDecimal.valueOf(42L));
//        order1.setTransactions(new HashSet<>());
//        order1.setUserId(123L);
//        transaction.setOrder(order1);
//        transaction.setPrice(BigDecimal.valueOf(42L));
//        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
//        transaction.setVolume(1L);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
//        TransactionService transactionService = new TransactionService(transactionRepository);
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(), 1L)).run());
//        verify(orderRepository).findById((Long) any());
//        verify(transactionRepository).save((Transaction) any());
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
//     */
//    @Test
//    void testExecuteOrderTaskRun5() {
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
//
//        Order order1 = new Order();
//        order1.setActive(true);
//        order1.setAllOrNone(true);
//        order1.setAmount(10);
//        order1.setCost(BigDecimal.valueOf(42L));
//        order1.setFee(BigDecimal.valueOf(42L));
//        order1.setLimitPrice(BigDecimal.valueOf(42L));
//        order1.setMargin(BigDecimal.valueOf(42L));
//        order1.setOrderId(123L);
//        order1.setSecurityId(123L);
//        order1.setSecurityType(SecurityType.STOCKS);
//        order1.setStopPrice(BigDecimal.valueOf(42L));
//        order1.setTransactions(new HashSet<>());
//        order1.setUserId(123L);
//
//        Transaction transaction = new Transaction();
//        transaction.setId(123L);
//        transaction.setOrder(order1);
//        transaction.setPrice(BigDecimal.valueOf(42L));
//        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
//        transaction.setVolume(1L);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
//        TransactionService transactionService = new TransactionService(transactionRepository);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        BigDecimal margin = BigDecimal.valueOf(42L);
//        BigDecimal limitPrice = BigDecimal.valueOf(42L);
//        BigDecimal stopPrice = BigDecimal.valueOf(42L);
//        BigDecimal fee = BigDecimal.valueOf(42L);
//        BigDecimal cost = BigDecimal.valueOf(42L);
//        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(123L, 123L, 123L, 10,
//                SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee, cost, true, new HashSet<>()), 1L)).run());
//        verify(orderRepository).findById((Long) any());
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
//     */
//    @Test
//    void testExecuteOrderTaskRun6() {
//        Order order = new Order();
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//
//        Transaction transaction = new Transaction();
//        transaction.setId(123L);
//
//        Order order1 = new Order();
//        order1.setActive(true);
//        order1.setAllOrNone(true);
//        order1.setAmount(10);
//        order1.setCost(BigDecimal.valueOf(42L));
//        order1.setFee(BigDecimal.valueOf(42L));
//        order1.setLimitPrice(BigDecimal.valueOf(42L));
//        order1.setMargin(BigDecimal.valueOf(42L));
//        order1.setOrderId(123L);
//        order1.setSecurityId(123L);
//        order1.setSecurityType(SecurityType.STOCKS);
//        order1.setStopPrice(BigDecimal.valueOf(42L));
//        order1.setTransactions(new HashSet<>());
//        order1.setUserId(123L);
//        transaction.setOrder(order1);
//        transaction.setPrice(BigDecimal.valueOf(42L));
//        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
//        transaction.setVolume(1L);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
//        TransactionService transactionService = new TransactionService(transactionRepository);
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        assertThrows(ArithmeticException.class, () -> (orderService.new ExecuteOrderTask(10, new Order(), 0L)).run());
//        verify(orderRepository).findById((Long) any());
//        verify(transactionRepository).save((Transaction) any());
//    }
//
//    /**
//     * Method under test: {@link OrderService.ExecuteOrderTask#run()}
//     */
//    @Test
//    @Disabled
//    void testExecuteOrderTaskRun7() {
//        BigDecimal margin = BigDecimal.valueOf(42L);
//        BigDecimal limitPrice = BigDecimal.valueOf(42L);
//        BigDecimal stopPrice = BigDecimal.valueOf(42L);
//        BigDecimal fee = BigDecimal.valueOf(42L);
//        BigDecimal cost = BigDecimal.valueOf(42L);
//
//        Order order = new Order(123L, 123L, 123L, 10, SecurityType.STOCKS, true, margin, limitPrice, stopPrice, fee, cost,
//                true, new HashSet<>());
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        when(orderRepository.findById((Long) any())).thenReturn(Optional.of(order));
//
//        Order order1 = new Order();
//        order1.setActive(true);
//        order1.setAllOrNone(true);
//        order1.setAmount(10);
//        order1.setCost(BigDecimal.valueOf(42L));
//        order1.setFee(BigDecimal.valueOf(42L));
//        order1.setLimitPrice(BigDecimal.valueOf(42L));
//        order1.setMargin(BigDecimal.valueOf(42L));
//        order1.setOrderId(123L);
//        order1.setSecurityId(123L);
//        order1.setSecurityType(SecurityType.STOCKS);
//        order1.setStopPrice(BigDecimal.valueOf(42L));
//        order1.setTransactions(new HashSet<>());
//        order1.setUserId(123L);
//
//        Transaction transaction = new Transaction();
//        transaction.setId(123L);
//        transaction.setOrder(order1);
//        transaction.setPrice(BigDecimal.valueOf(42L));
//        transaction.setTime(LocalDateTime.of(1, 1, 1, 1, 1));
//        transaction.setVolume(1L);
//        TransactionRepository transactionRepository = mock(TransactionRepository.class);
//        when(transactionRepository.save((Transaction) any())).thenReturn(transaction);
//        TransactionService transactionService = new TransactionService(transactionRepository);
//        OrderMapper orderMapper = new OrderMapper(new TransactionMapper());
//        OrderService orderService = new OrderService(orderRepository, orderMapper, transactionService,
//                new FormulaCalculator());
//
//        (orderService.new ExecuteOrderTask(10, new Order(), 1000L)).run();
//        verify(orderRepository).findById((Long) any());
//        verify(transactionRepository).save((Transaction) any());
//    }
//
//    /**
//     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
//     */
//    @Test
//    @Disabled("JWS")
//    void testFindOrderForUser() {
//
//        OrderDto userOrder = this.orderService.findOrderForUser(123L, "xxxxxxx.yyyyyyyyy.zzzzz");
//        assertEquals(123L, userOrder.getOrderId());
//    }
//
//    /**
//     * Method under test: {@link OrderService#findOrderForUser(Long, String)}
//     */
//    @Test
//    @Disabled("JWS")
//    void testFindOrderForUser2() {
//
//        try {
//            OrderDto userOrder = this.orderService.findOrderForUser(123L, "x.y.z");
//        } catch (OrderNotFoundException e) {
//            assertEquals("No order with given id could be found for user", e.getMessage());
//        }
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_InsideExecuteLimitOrder_ForOrderWithOnlyLimitPrice() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(null);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        OrderMapper orderMapper1 = new OrderMapper(new TransactionMapper());
//        OrderDto orderDto = orderMapper1.orderToOrderDto(order);
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(orderDto, "jws");
//
//        verify(orderServiceSpy, times(1)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(0)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_InsideExecuteLimitOrder_ForOrderWithBothPrices() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(new OrderDto(), "jws");
//
//        verify(orderServiceSpy, times(1)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(0)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_InsideExecuteLimitOrder_ForOrderWithBothPrices2() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(-10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.ZERO);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(new OrderDto(), "jws");
//
//        verify(orderServiceSpy, times(1)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(0)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_ForOrderWithBothPrices() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.ZERO);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(new OrderDto(), "jws");
//
//        verify(orderServiceSpy, times(0)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(0)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_InsideExecuteMarketOrder_ForOrderWithNoPrices() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(null);
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(null);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(new OrderDto(), "jws");
//
//        verify(orderServiceSpy, times(0)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(1)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_InsideExecuteMarketOrder_ForOrderWithOnlyStopPrice() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(null);
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(new OrderDto(), "jws");
//
//        verify(orderServiceSpy, times(0)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(1)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_InsideExecuteMarketOrder_ForOrderWithOnlyStopPrice2() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(-10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(null);
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.ZERO);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(new OrderDto(), "jws");
//
//        verify(orderServiceSpy, times(0)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(1)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void createOrder_CallsOrderRepositorySave_Once_ForOrderWithOnlyStopPrice() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        SecurityDto securityDto = new SecurityDto();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(null);
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.ZERO);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        securityDto.setId(1);
//        securityDto.setTicker("TICKER");
//        securityDto.setName("NAME");
//        securityDto.setLastUpdated("LAST_UPDATED");
//        securityDto.setPrice(BigDecimal.ONE);
//        securityDto.setAsk(BigDecimal.ONE);
//        securityDto.setBid(BigDecimal.ONE);
//        securityDto.setChange(BigDecimal.ONE);
//        securityDto.setVolume(1L);
//        securityDto.setContractSize(1);
//        securityDto.setSecurityHistory(new ArrayList<>());
//        securityDto.setChangePercent(BigDecimal.ZERO);
//        securityDto.setDollarVolume(BigDecimal.ONE);
//        securityDto.setNominalValue(BigDecimal.ONE);
//        securityDto.setInitialMarginCost(BigDecimal.ONE);
//        securityDto.setMaintenanceMargin(BigDecimal.ONE);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        when(orderMapper.orderDtoToOrder(any())).thenReturn(order);
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        doReturn(securityDto).when(orderServiceSpy).getSecurityByTypeAndId(order.getSecurityType(), order.getSecurityId());
//
//        orderServiceSpy.createOrder(new OrderDto(), "jws");
//
//        verify(orderServiceSpy, times(0)).executeLimitOrder(any(), any(), any(), any(), any(), any());
//        verify(orderServiceSpy, times(0)).executeMarketOrder(any(), any(), any(), any());
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    public void findAllOrdersForUser_CallsOrderRepositoryFindAllByUserId_Once() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        List<Order> orderList = new ArrayList<>();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(null);
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.ZERO);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        orderList.add(order);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        OrderMapper orderMapper1 = new OrderMapper(new TransactionMapper());
//        List<OrderDto> orderDtoList = orderMapper1.ordersToOrderDtos(orderList);
//
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        when(orderRepository.findAllByUserId(any())).thenReturn(orderList);
//        when(orderMapper.ordersToOrderDtos(orderList)).thenReturn(orderDtoList);
//
//        List<OrderDto> actual = orderServiceSpy.findAllOrdersForUser("jws");
//
//        assertEquals(orderDtoList, actual);
//        verify(orderRepository, times(1)).findAllByUserId(any());
//    }
//
//    @Test
//    public void findOrderForUser_CallsOrderRepositoryFindByOrderIdAndUserId_Once() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(null);
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.ZERO);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        OrderMapper orderMapper1 = new OrderMapper(new TransactionMapper());
//        OrderDto orderDto = orderMapper1.orderToOrderDto(order);
//
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        when(orderRepository.findByOrderIdAndUserId(any(), any())).thenReturn(Optional.of(order));
//        when(orderMapper.orderToOrderDto(order)).thenReturn(orderDto);
//
//        OrderDto actual = orderServiceSpy.findOrderForUser(1L, "jws");
//
//        assertEquals(orderDto, actual);
//        verify(orderRepository, times(1)).findByOrderIdAndUserId(any(), any());
//    }
//
//    @Test
//    public void updateOrder_throwsUpdateNotAllowedException_ForOrderPricesNull() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(null);
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(null);
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        when(orderRepository.findByOrderIdAndUserId(any(), any())).thenReturn(Optional.of(order));
//
//        Exception exception = assertThrows(UpdateNotAllowedException.class, () -> {
//            orderServiceSpy.updateOrder(new OrderDto(), "jws");
//        });
//
//        String expected = "Market orders can't be updated once they're submitted";
//        String actual = exception.getMessage();
//
//        assertEquals(expected, actual);
//        verify(orderRepository, times(1)).findByOrderIdAndUserId(any(), any());
//    }
//
//    @Test
//    public void updateOrder_callsOrderRepositorySave_Once_ForOrderWithPrices() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        when(orderRepository.findByOrderIdAndUserId(any(), any())).thenReturn(Optional.of(order));
//
//        OrderMapper orderMapper1 = new OrderMapper(new TransactionMapper());
//        OrderDto orderDto = orderMapper1.orderToOrderDto(order);
//
//        orderServiceSpy.updateOrder(orderDto, "jws");
//        verify(orderRepository, times(1)).save(order);
//        verify(orderRepository, times(1)).findByOrderIdAndUserId(any(), any());
//    }
//
//    @Test
//    public void deleteOrder_callsOrderRepositorySave_and_callsOrderSetActive_Once() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        Order orderSpy = spy(order);
//
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        when(orderRepository.findByOrderIdAndUserId(any(), any())).thenReturn(Optional.of(orderSpy));
//
//        orderServiceSpy.deleteOrder(1L, "jws");
//
//        verify(orderSpy, times(1)).setActive(Boolean.FALSE);
//        verify(orderRepository, times(1)).save(orderSpy);
//    }
//
//    @Test
//    public void deleteAllOrdersForUser_callsOrderRepositorySaveAll_Once_and_callsOrderSetActive_ForEveryOrder() {
//        OrderService orderServiceSpy = spy(orderService);
//        Order order = new Order();
//        Order order2 = new Order();
//        Order order3 = new Order();
//        List<Order> orderSpyList = new ArrayList<>();
//
//        orderSpyList.add(spy(order));
//        orderSpyList.add(spy(order2));
//        orderSpyList.add(spy(order3));
//        order.setActive(true);
//        order.setAllOrNone(true);
//        order.setAmount(10);
//        order.setCost(BigDecimal.valueOf(42L));
//        order.setFee(BigDecimal.valueOf(42L));
//        order.setLimitPrice(BigDecimal.valueOf(42L));
//        order.setMargin(BigDecimal.valueOf(42L));
//        order.setOrderId(123L);
//        order.setSecurityId(123L);
//        order.setSecurityType(SecurityType.STOCKS);
//        order.setStopPrice(BigDecimal.valueOf(42L));
//        order.setTransactions(new HashSet<>());
//        order.setUserId(123L);
//        order2.setActive(true);
//        order2.setAllOrNone(true);
//        order2.setAmount(10);
//        order2.setCost(BigDecimal.valueOf(42L));
//        order2.setFee(BigDecimal.valueOf(42L));
//        order2.setLimitPrice(BigDecimal.valueOf(42L));
//        order2.setMargin(BigDecimal.valueOf(42L));
//        order2.setOrderId(123L);
//        order2.setSecurityId(123L);
//        order2.setSecurityType(SecurityType.STOCKS);
//        order2.setStopPrice(BigDecimal.valueOf(42L));
//        order2.setTransactions(new HashSet<>());
//        order2.setUserId(123L);
//        order3.setActive(true);
//        order3.setAllOrNone(true);
//        order3.setAmount(10);
//        order3.setCost(BigDecimal.valueOf(42L));
//        order3.setFee(BigDecimal.valueOf(42L));
//        order3.setLimitPrice(BigDecimal.valueOf(42L));
//        order3.setMargin(BigDecimal.valueOf(42L));
//        order3.setOrderId(123L);
//        order3.setSecurityId(123L);
//        order3.setSecurityType(SecurityType.STOCKS);
//        order3.setStopPrice(BigDecimal.valueOf(42L));
//        order3.setTransactions(new HashSet<>());
//        order3.setUserId(123L);
//
//        List<Order> orderList = new ArrayList<>(orderSpyList);
//
//        String usercrudApiUrl = "http://localhost:8091";
//        ReflectionTestUtils.setField(orderServiceSpy, "usercrudApiUrl", usercrudApiUrl);
//
//        doReturn("car@gmail.com").when(orderServiceSpy).extractUsername(any());
//        when(orderRepository.findAllByUserIdAndActive(any(), any())).thenReturn(orderList);
//
//        orderServiceSpy.deleteAllOrdersForUser("jws");
//
//        for (Order spy : orderList) {
//            verify(spy, times(1)).setActive(Boolean.FALSE);
//        }
//        verify(orderRepository, times(1)).saveAll(orderList);
//    }
//}
//
