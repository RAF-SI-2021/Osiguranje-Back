package buyingMarket.services;

import app.model.dto.SecurityDTO;
import buyingMarket.exceptions.OrderNotFoundException;
import buyingMarket.exceptions.UpdateNotAllowedException;
import buyingMarket.formulas.FormulaCalculator;
import buyingMarket.mappers.OrderMapper;
import buyingMarket.model.*;
import buyingMarket.model.dto.OrderDto;
import buyingMarket.repositories.OrderRepository;
import crudApp.dto.UserDto;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TransactionService transactionService;
    private final FormulaCalculator formulaCalculator;
    private final TaskScheduler taskScheduler;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper,
                        TransactionService transactionService,
                        FormulaCalculator formulaCalculator) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.transactionService = transactionService;
        this.formulaCalculator = formulaCalculator;
        this.taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
    }

    public void createOrder(OrderDto orderDto, String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        Order order = orderMapper.orderDtoToOrder(orderDto);
        OrderType orderType = order.getOrderType();
        SecurityType securityType = order.getSecurityType();
        Long securityId = order.getSecurityId();
        if(user == null) {
            throw new IllegalArgumentException("Something went wrong trying to find user");
        }
        if(orderType == null) {
            throw new IllegalArgumentException("Please provide an OrderType");
        }
        if(securityId == null) {
            throw new IllegalArgumentException("Please provide an SecurityId");
        }
        if(securityType == null) {
            throw new IllegalArgumentException("Please provide an SecurityType");
        }
        SecurityDTO security = getSecurityByTypeAndId(securityType, securityId);
        if(security == null) {
            throw new IllegalArgumentException("Something went wrong trying to find security");
        }
        Long volume = security.getVolume();
        if(security == null) {
            throw new IllegalArgumentException("Something went wrong retrieving security data");
        }
        switch (orderType) {
            case LIMIT: {
                Integer amount = order.getAmount();
                if(amount == null) {
                    throw new IllegalArgumentException("Limit order requires an amount, please provide one");
                }
                BigDecimal price = orderDto.getPrice();
                if(price == null) {
                    throw new IllegalArgumentException("Limit order requires a price, please provide one");
                }
                executeLimitOrder(order, amount, orderType, price, security, user, volume);
            }
            break;
            case STOP_LIMIT: {
                BigDecimal stopPrice = order.getStopPrice();
                if(stopPrice == null) {
                    throw new IllegalArgumentException("Stop-limit order requires a stop price, please provide one");
                }
                Integer amount = order.getAmount();
                if(amount == null) {
                    throw new IllegalArgumentException("Stop-limit order requires an amount, please provide one");
                }
                BigDecimal price = order.getPrice();
                if(price == null) {
                    throw new IllegalArgumentException("Stop-limit order requires a price, please provide one");
                }
                if(
                        (amount < 0 && stopPrice.compareTo(security.getBid()) < 0) ||
                        (amount > 0 && stopPrice.compareTo(security.getAsk()) > 0)
                ) {
                    executeLimitOrder(order, amount, orderType, price, security, user, volume);
                } else {
                    orderRepository.save(order);
                }
            }
                break;
            case MARKET: {
                Integer amount = order.getAmount();
                if(amount == null) {
                    throw new IllegalArgumentException("Market order requires an amount, please provide one");
                }
                executeMarketOrder(order, amount, orderType, security, user);
            }
            break;
            default:
        }
    }

    public List<OrderDto> findAllOrdersForUser(String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        List<Order> orders = orderRepository.findAllByUserId(user.getId());
        return orderMapper.ordersToOrderDtos(orders);
    }

    public OrderDto findOrderForUser(Long id, String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        Order order = orderRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new OrderNotFoundException("No order with given id could be found for user"));
        return orderMapper.orderToOrderDto(order);
    }

    public void updateOrder(OrderDto orderDto, String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        Order order = orderRepository.findByIdAndUserId(orderDto.getOrderId(), user.getId()).orElseThrow(() -> new OrderNotFoundException("No order with given id could be found for user"));
        switch(order.getOrderType()) {
            case MARKET:
                throw new UpdateNotAllowedException("Market orders can't be updated once they're submitted");
            case LIMIT: {
                Set<Transaction> transactions = order.getTransactions();
                long totalFilledAmount = 0;
                for(Transaction transaction: transactions) {
                    totalFilledAmount += transaction.getVolume();
                }
                if(Math.abs(order.getAmount()) == totalFilledAmount) {
                    throw new UpdateNotAllowedException("Order has been fully filled already");
                }
                if(Math.signum(order.getAmount()) != Math.signum(orderDto.getAmount())) {
                    throw new UpdateNotAllowedException("Orders can't switch sides");
                } else if(Math.abs(orderDto.getAmount()) < totalFilledAmount) {
                    throw new UpdateNotAllowedException("Can't reduce size to less than what is already filled");
                }
                order.setAmount(orderDto.getAmount());
                order.setPrice(orderDto.getPrice());
                order.setMargin(orderDto.getMargin());
                orderRepository.save(order);
            }
                break;
            case STOP_LIMIT: {
                Set<Transaction> transactions = order.getTransactions();
                long totalFilledAmount = 0;
                for(Transaction transaction: transactions) {
                    totalFilledAmount += transaction.getVolume();
                }
                if(Math.abs(order.getAmount()) == totalFilledAmount) {
                    throw new UpdateNotAllowedException("Order has been fully filled already");
                }
                if(Math.signum(order.getAmount()) != Math.signum(orderDto.getAmount())) {
                    throw new UpdateNotAllowedException("Orders can't switch sides");
                } else if(Math.abs(orderDto.getAmount()) < totalFilledAmount) {
                    throw new UpdateNotAllowedException("Can't reduce size to less than what is already filled");
                }
                order.setAmount(orderDto.getAmount());
                order.setPrice(orderDto.getPrice());
                order.setStopPrice(orderDto.getStopPrice());
                order.setMargin(orderDto.getMargin());
                orderRepository.save(order);
            }
                break;
            default:
        }
    }

    public void deleteOrder(Long id, String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        Order order = orderRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new OrderNotFoundException("No order with given id could be found for user"));
        order.setActive(Boolean.FALSE);
        orderRepository.save(order);
    }

    public void deleteAllOrdersForUser(String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        List<Order> orders = orderRepository.findAllByUserIdAndActive(user.getId(), Boolean.TRUE);
        orders.stream().forEach(order -> {order.setActive(Boolean.FALSE);});
        orderRepository.saveAll(orders);
    }

    private String extractUsername(String jws) {
        String username = Jwts.parserBuilder()
                .setSigningKey(jwtSecret).build()
                .parseClaimsJws(jws).getBody().getSubject();
        return username;
    }

    private UserDto getUserByUsernameFromUserService(String username) {
        String urlString = "http://localhost:8091/api/users/search/email";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(urlString);
        String urlTemplate = uriComponentsBuilder.queryParam("email", username).encode().toUriString();
        RestTemplate rest = new RestTemplate();
        ResponseEntity<UserDto> response = null;
        response = rest.exchange(urlTemplate, HttpMethod.GET, null, UserDto.class);
        UserDto user = response.getBody();
        return user;
    }

    private SecurityDTO getSecurityByTypeAndId(SecurityType securityType, Long securityId) {
        StringBuilder sb = new StringBuilder("http://localhost:2000/api/data/");
        sb.append(securityType.toString().toLowerCase()).append(securityId);
        String urlString = sb.toString();
        RestTemplate rest = new RestTemplate();
        ResponseEntity<SecurityDTO> response = null;
        response = rest.exchange(urlString, HttpMethod.GET, null, SecurityDTO.class);
        SecurityDTO security = response.getBody();
        return security;
    }

    private void executeLimitOrder(Order order, Integer amount, OrderType orderType, BigDecimal price, SecurityDTO security, UserDto user, Long volume){
        BigDecimal cost;
        if(amount > 0 ) {
            order.setFee(formulaCalculator.calculateSecurityFee(orderType, (long) Math.abs(amount), security.getAsk(), price));
            cost = price.compareTo(security.getAsk()) > 1 ?
                    security.getAsk().multiply(BigDecimal.valueOf(amount)) :
                    price.multiply(BigDecimal.valueOf(amount));
        } else {
            order.setFee(formulaCalculator.calculateSecurityFee(orderType, (long) Math.abs(amount), security.getBid(), price));
            cost = price.compareTo(security.getBid()) > 1 ?
                    security.getBid().multiply(BigDecimal.valueOf(amount)) :
                    price.multiply(BigDecimal.valueOf(amount));
        }
        order.setCost(cost);
        order.setUserId(user.getId());
        orderRepository.save(order);
        long waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (volume / Math.abs(amount))) * 1000L;
        taskScheduler.schedule(new ExecuteOrderTask(amount, order, volume), new Date(System.currentTimeMillis() + waitTime));
    }

    private void executeMarketOrder(Order order,Integer amount,OrderType orderType,SecurityDTO security,UserDto user) {
        BigDecimal cost;
        if(amount > 0 ) {
            order.setFee(formulaCalculator.calculateSecurityFee(orderType, (long) Math.abs(amount), security.getAsk()));
            cost = security.getAsk().multiply(BigDecimal.valueOf(amount));
        } else {
            order.setFee(formulaCalculator.calculateSecurityFee(orderType, (long) Math.abs(amount), security.getBid()));
            cost = security.getBid().multiply(BigDecimal.valueOf(amount));
        }
        order.setCost(cost);
        order.setUserId(user.getId());
        order = orderRepository.save(order);
        Transaction transaction = Transaction.builder()
                .time(LocalDateTime.now())
                .price(amount > 0 ? security.getAsk() : security.getBid())
                .volume((long) amount)
                .order(order)
                .build();
        transactionService.save(transaction);
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
            if(amountNotFilled > 0) {
                int amountFilled = ThreadLocalRandom.current().nextInt(amountNotFilled);
                Order orderFromRepo = orderRepository.findById(order.getOrderId()).orElse(order);
                if(orderFromRepo.getActive().booleanValue()) {
                    amountNotFilled -= amountFilled;
                    Transaction transaction = Transaction.builder()
                            .time(LocalDateTime.now())
                            .price(order.getPrice())
                            .volume((long) amountFilled)
                            .order(order)
                            .build();
                    transactionService.save(transaction);
                    long waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (volume / Math.abs(amountNotFilled))) * 1000L;
                    taskScheduler.schedule(new ExecuteOrderTask(amountNotFilled, order, volume), new Date(System.currentTimeMillis() + waitTime));
                }
            }
        }
    }
}