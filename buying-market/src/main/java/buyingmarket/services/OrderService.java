package buyingmarket.services;

import buyingmarket.exceptions.OrderNotFoundException;
import buyingmarket.exceptions.UpdateNotAllowedException;
import buyingmarket.formulas.FormulaCalculator;
import buyingmarket.mappers.OrderMapper;
import buyingmarket.model.*;
import buyingmarket.model.dto.OrderCreateDto;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.model.dto.SecurityDto;
import buyingmarket.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {
    private ActuaryService actuaryService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private TransactionService transactionService;
    private TaskScheduler taskScheduler;
    private RestTemplate rest;
    private static final String ORDER_NOT_FOUND_ERROR = "No order with given id could be found for user";

    @Value("${api.securities}")
    private String securitiesApiUrl;

    public OrderService() {}

    @Autowired
    public OrderService(ActuaryService actuaryService,
                        OrderRepository orderRepository,
                        OrderMapper orderMapper,
                        TransactionService transactionService) {
        this.actuaryService = actuaryService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.transactionService = transactionService;
        this.taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
        this.rest = new RestTemplate();
    }

    public void validateOrder(Long orderId, OrderState orderState, String jws) {
        Optional<Order> o = orderRepository.findById(orderId);
        if (o.isPresent()) {
            Order order = o.get();
            order.setOrderState(orderState);
            order.setApprovingActuary((Supervisor) actuaryService.getActuary(jws));
            orderRepository.save(order);
            if (orderState.equals(OrderState.APPROVED)) {
                execute(order);
            }
        } else {
            throw new UpdateNotAllowedException("Order not found.");
        }
    }

    public void createOrder(OrderCreateDto orderCreateDto, String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        Order order = orderMapper.orderCreateDtoToOrder(orderCreateDto);
        order.setActuary(actuary);
        SecurityDto security = getSecurityFromOrder(order);
        if (actuary instanceof Agent) {
            Agent agent = (Agent) actuary;
            if (agent.getApprovalRequired() || agent.getSpendingLimit().compareTo(agent.getUsedLimit().add(FormulaCalculator.getEstimatedValue(order, security))) <= 0) {
                order.setOrderState(OrderState.WAITING);
            }
        }
        order.setModificationDate(new Date());
        orderRepository.save(order);
        if (order.getOrderState().equals(OrderState.APPROVED)) {
            execute(order);
        }
    }

    public List<OrderDto> findAllOrdersForUser(String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        List<Order> orders = orderRepository.findAllByActuary(actuary);
        return orderMapper.ordersToOrderDtos(orders);
    }

    public OrderDto findOrderForUser(Long id, String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        Order order = orderRepository.findByOrderIdAndActuary(id, actuary).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_ERROR));
        return orderMapper.orderToOrderDto(order);
    }

    public void deleteOrder(Long id, String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        Order order = orderRepository.findByOrderIdAndActuary(id, actuary).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_ERROR));
        order.setOrderState(OrderState.DECLINED);
        order.setModificationDate(new Date());
        orderRepository.save(order);
    }

    public void deleteAllOrdersForUser(String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        List<Order> orders = orderRepository.findAllByActuaryAndActive(actuary, Boolean.TRUE);
        orders.forEach(order -> order.setOrderState(OrderState.DECLINED));
        orders.forEach(order -> order.setModificationDate(new Date()));
        orderRepository.saveAll(orders);
    }

    protected SecurityDto getSecurityFromOrder(Order order) {
        String urlString = securitiesApiUrl + "/api/data/" + order.getSecurityType().toString().toLowerCase() + "/" + order.getSecurityId();
        ResponseEntity<SecurityDto> response = rest.exchange(urlString, HttpMethod.GET, null, SecurityDto.class);
        SecurityDto security = null;
        if(response.getBody() != null) {
            security = response.getBody();
        }
        if (security == null) {
            throw new IllegalArgumentException("Something went wrong trying to find security");
        }
        return security;
    }

    protected void execute(Order order) {
        SecurityDto security = getSecurityFromOrder(order);
        order.setFee(FormulaCalculator.calculateSecurityFee(order, security));
        order.setModificationDate(new Date());
        orderRepository.save(order);
        taskScheduler.schedule(new ExecuteOrderTask(order, order.getStopPrice() == null), new Date(FormulaCalculator.waitTime(security.getVolume(), order.getAmount())));
    }

    public class ExecuteOrderTask implements Runnable {

        private Order order;
        private Boolean stopFlag;

        public ExecuteOrderTask(Order order, Boolean stopFlag) {
            this.order = order;
            this.stopFlag = stopFlag;
        }

        @Override
        public void run() {
            int amountLeft = order.getAmount() - order.getAmountFilled();
            Order orderFromRepo = orderRepository.findById(order.getOrderId()).orElse(order);
            if (!orderFromRepo.getOrderState().equals(OrderState.DECLINED) && amountLeft <= 0) {
                SecurityDto security = getSecurityFromOrder(order);
                if (stopFlag && stopCheck(order, security)) {
                    stopFlag = false;
                }
                if (priceCheck(order, security) && !stopFlag) {
                    int executeAmount = order.getAllOrNone() ? order.getAmount() : ThreadLocalRandom.current().nextInt(amountLeft);
                    order.setAmountFilled(order.getAmountFilled() + executeAmount);
                    order.setModificationDate(new Date());
                    orderRepository.save(order);
                    Transaction transaction = Transaction.builder()
                            .time(LocalDateTime.now())
                            .price(order.getLimitPrice())
                            .volume((long) executeAmount)
                            .order(order)
                            .build();
                    transactionService.save(transaction);
                }
                taskScheduler.schedule(new ExecuteOrderTask(order, stopFlag), new Date(FormulaCalculator.waitTime(security.getVolume(), order.getAmount())));
            }
        }

        public Order getOrder() {
            return order;
        }

        private boolean priceCheck(Order order, SecurityDto security) {
            if (order.getLimitPrice() == null) {
                return true;
            }
            if (order.getActionType().equals(ActionType.BUY) && security.getAsk().compareTo(order.getLimitPrice()) <= 0) {
                return true;
            }
            return order.getActionType().equals(ActionType.SELL) && security.getBid().compareTo(order.getLimitPrice()) >= 0;
        }

        private boolean stopCheck(Order order, SecurityDto security) {
            if (order.getActionType().equals(ActionType.BUY) && security.getAsk().compareTo(order.getStopPrice()) >= 0) {
                return true;
            }
            return order.getActionType().equals(ActionType.SELL) && security.getBid().compareTo(order.getStopPrice()) <= 0;
        }
    }
}
