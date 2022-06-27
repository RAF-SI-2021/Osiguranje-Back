package buyingmarket.services;

import buyingmarket.exceptions.OrderNotFoundException;
import buyingmarket.exceptions.SecurityNotFoundException;
import buyingmarket.exceptions.UpdateNotAllowedException;
import buyingmarket.formulas.FormulaCalculator;
import buyingmarket.mappers.OrderMapper;
import buyingmarket.model.*;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.model.dto.SecurityDto;
import buyingmarket.model.dto.UserDto;
import buyingmarket.repositories.ActuaryRepository;
import buyingmarket.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private static final String ORDER_FULLY_FILLED_ERROR = "Order has been fully filled already";
    private static final String ORDER_SIDE_ERROR = "Orders can't switch sides";
    private static final String ORDER_REDUCE_ERROR = "Can't reduce size to less than what is already filled";

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

    public void createOrder(OrderDto orderDto, String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        Order order = orderMapper.orderDtoToOrder(orderDto);
        order.setActuary(actuary);
        SecurityDto security = getSecurityFromOrder(order);
        Long volume = security.getVolume();
        if(order.getLimitPrice() != null && order.getStopPrice() == null) {
            Integer amount = order.getAmount();
            BigDecimal limitPrice = orderDto.getLimitPrice();
            executeLimitOrder(order, amount, limitPrice, volume);
        } else if(order.getLimitPrice() != null && order.getStopPrice() != null) {
            BigDecimal stopPrice = order.getStopPrice();
            Integer amount = order.getAmount();
            BigDecimal limitPrice = order.getLimitPrice();
            if((amount < 0 && stopPrice.compareTo(security.getBid()) < 0) || (amount > 0 && stopPrice.compareTo(security.getAsk()) > 0)) {
                executeLimitOrder(order, amount, limitPrice, volume);
            } else {
                orderRepository.save(order);
            }
        } else if(order.getLimitPrice() == null && order.getStopPrice() == null) {
            Integer amount = order.getAmount();
            executeMarketOrder(order, amount);
        } else {
            BigDecimal stopPrice = order.getStopPrice();
            Integer amount = order.getAmount();
            if((amount < 0 && stopPrice.compareTo(security.getBid()) < 0) || (amount > 0 && stopPrice.compareTo(security.getAsk()) > 0)) {
                executeMarketOrder(order, amount);
            } else {
                orderRepository.save(order);
            }
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

    public void updateOrder(OrderDto orderDto, String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        Order order = orderRepository.findByOrderIdAndActuary(orderDto.getOrderId(), actuary).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_ERROR));
        if(order.getLimitPrice() == null && order.getStopPrice() == null) {
            throw new UpdateNotAllowedException("Market orders can't be updated once they're submitted");
        }
        else{
            Set<Transaction> transactions = order.getTransactions();
            long totalFilledAmount = 0;
            for(Transaction transaction: transactions) {
                totalFilledAmount += transaction.getVolume();
            }
            if(Math.abs(order.getAmount()) == totalFilledAmount) {
                throw new UpdateNotAllowedException(ORDER_FULLY_FILLED_ERROR);
            }
            if(transactions.isEmpty()) {
                order.setAllOrNone(orderDto.getAllOrNone());
            }
            if(Math.signum(order.getAmount()) != Math.signum(orderDto.getAmount())) {
                throw new UpdateNotAllowedException(ORDER_SIDE_ERROR);
            } else if(Math.abs(orderDto.getAmount()) < totalFilledAmount) {
                throw new UpdateNotAllowedException(ORDER_REDUCE_ERROR);
            }
            order.setAmount(orderDto.getAmount());
            order.setMargin(orderDto.getMargin());
            if(order.getLimitPrice() != null)
                order.setLimitPrice(orderDto.getLimitPrice());
            if(order.getStopPrice() != null)
                order.setStopPrice(orderDto.getStopPrice());
            orderRepository.save(order);
        }
    }

    public void deleteOrder(Long id, String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        Order order = orderRepository.findByOrderIdAndActuary(id, actuary).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_ERROR));
        order.setActive(Boolean.FALSE);
        orderRepository.save(order);
    }

    public void deleteAllOrdersForUser(String jws) {
        Actuary actuary = actuaryService.getActuary(jws);
        List<Order> orders = orderRepository.findAllByActuaryAndActive(actuary, Boolean.TRUE);
        orders.forEach(order -> order.setActive(Boolean.FALSE));
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

    protected void executeLimitOrder(Order order, Integer amount, BigDecimal price, Long volume){
        SecurityDto security = getSecurityFromOrder(order);
        BigDecimal cost;
        if(amount > 0 ) {
            order.setFee(FormulaCalculator.calculateSecurityFee(order, security.getAsk(), price));
            cost = price.compareTo(security.getAsk()) > 1 ?
                    security.getAsk().multiply(BigDecimal.valueOf(amount)) :
                    price.multiply(BigDecimal.valueOf(amount));
        } else {
            order.setFee(FormulaCalculator.calculateSecurityFee(order, security.getBid(), price));
            cost = price.compareTo(security.getBid()) > 1 ?
                    security.getBid().multiply(BigDecimal.valueOf(amount)) :
                    price.multiply(BigDecimal.valueOf(amount));
        }
        order.setCost(cost);
        orderRepository.save(order);
        long waitTime = ThreadLocalRandom.current().nextLong(24 * 60) * 1000L;;
        if (volume > Math.abs(amount)) {
            waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (volume / Math.abs(amount))) * 1000L;
        }
        taskScheduler.schedule(new ExecuteOrderTask(amount, order, volume), new Date(System.currentTimeMillis() + waitTime));
    }

    protected void executeMarketOrder(Order order, Integer amount) {
        SecurityDto security = getSecurityFromOrder(order);
        BigDecimal cost;
        if(amount > 0 ) {
            order.setFee(FormulaCalculator.calculateSecurityFee(order, security.getAsk()));
            cost = security.getAsk().multiply(BigDecimal.valueOf(amount));
        } else {
            order.setFee(FormulaCalculator.calculateSecurityFee(order, security.getBid()));
            cost = security.getBid().multiply(BigDecimal.valueOf(amount));
        }
        order.setCost(cost);
        order = orderRepository.save(order);
        long waitTime = ThreadLocalRandom.current().nextLong(24 * 60) * 1000L;;
        if (security.getVolume() > Math.abs(amount)) {
            waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (security.getVolume() / Math.abs(amount))) * 1000L;
        }
        taskScheduler.schedule(new ExecuteOrderTask(amount, order, security.getVolume()), new Date(System.currentTimeMillis() + waitTime));
    }

    public class ExecuteOrderTask implements Runnable {

        private int amount;
        private Order order;
        private Long volume;

        public ExecuteOrderTask(int amount, Order order, Long volume) {
            this.amount = amount;
            this.order = order;
            this.volume = volume;
        }

        @Override
        public void run() {
            int amountNotFilled = Math.abs(amount);
            int amountFilled = ThreadLocalRandom.current().nextInt(amountNotFilled);
            Order orderFromRepo = orderRepository.findById(order.getOrderId()).orElse(order);
            if (orderFromRepo.getActive()) {
                Boolean allOrNone = order.getAllOrNone();
                if (allOrNone != null && allOrNone && amountNotFilled != amountFilled && amountNotFilled > 0) {
                    getWaitTime(amountNotFilled);
                } else {
                    amountNotFilled -= amountFilled;
                    Transaction transaction = Transaction.builder()
                            .time(LocalDateTime.now())
                            .price(order.getLimitPrice())
                            .volume((long) amountFilled)
                            .order(order)
                            .build();
                    transactionService.save(transaction);
                    if (amountNotFilled > 0) {
                        getWaitTime(amountNotFilled);
                    }
                }
            }
        }

        private void getWaitTime(int amountNotFilled) {
            long waitTime = ThreadLocalRandom.current().nextLong(24 * 60) * 1000L;
            if (volume > Math.abs(amountNotFilled)) {
                waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (volume / Math.abs(amountNotFilled))) * 1000L;
            }

            taskScheduler.schedule(new ExecuteOrderTask(amountNotFilled, order, volume), new Date(System.currentTimeMillis() + waitTime));
        }

        public int getAmount() {
            return amount;
        }

        public Order getOrder() {
            return order;
        }

        public Long getVolume() {
            return volume;
        }
    }
}
