package buyingmarket.services;

import buyingmarket.exceptions.OrderNotFoundException;
import buyingmarket.exceptions.SecurityNotFoundException;
import buyingmarket.exceptions.UpdateNotAllowedException;
import buyingmarket.exceptions.UserNotFoundException;
import buyingmarket.formulas.FormulaCalculator;
import buyingmarket.mappers.OrderMapper;
import buyingmarket.model.*;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.model.dto.SecurityDto;
import buyingmarket.model.dto.UserDto;
import buyingmarket.repositories.OrderRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.Key;
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
    private static final String ORDER_NOT_FOUND_ERROR = "No order with given id could be found for user";
    private static final String ORDER_FULLY_FILLED_ERROR = "Order has been fully filled already";
    private static final String ORDER_SIDE_ERROR = "Orders can't switch sides";
    private static final String ORDER_REDUCE_ERROR = "Can't reduce size to less than what is already filled";

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
        SecurityType securityType = order.getSecurityType();
        Long securityId = order.getSecurityId();
        SecurityDto security = getSecurityByTypeAndId(securityType, securityId);
        Long volume = security.getVolume();
        if(order.getLimitPrice() != null && order.getStopPrice() == null) {
            Integer amount = order.getAmount();
            BigDecimal limitPrice = orderDto.getLimitPrice();
            executeLimitOrder(order, amount, limitPrice, security, user, volume);
        } else if(order.getLimitPrice() != null && order.getStopPrice() != null) {
            BigDecimal stopPrice = order.getStopPrice();
            Integer amount = order.getAmount();
            BigDecimal limitPrice = order.getLimitPrice();

            if(
                    (amount < 0 && stopPrice.compareTo(security.getBid()) < 0) ||
                            (amount > 0 && stopPrice.compareTo(security.getAsk()) > 0)
            ) {
                executeLimitOrder(order, amount, limitPrice, security, user, volume);
            } else {
                orderRepository.save(order);
            }
        } else if(order.getLimitPrice() == null && order.getStopPrice() == null) {
            Integer amount = order.getAmount();
            executeMarketOrder(order, amount, security, user);
        } else {
            BigDecimal stopPrice = order.getStopPrice();
            Integer amount = order.getAmount();
            if(
                    (amount < 0 && stopPrice.compareTo(security.getBid()) < 0) ||
                            (amount > 0 && stopPrice.compareTo(security.getAsk()) > 0)
            ) {
                executeMarketOrder(order, amount, security, user);
            } else {
                orderRepository.save(order);
            }
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
        Order order = orderRepository.findByOrderIdAndUserId(id, user.getId()).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_ERROR));
        return orderMapper.orderToOrderDto(order);
    }

    public void updateOrder(OrderDto orderDto, String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        Order order = orderRepository.findByOrderIdAndUserId(orderDto.getOrderId(), user.getId()).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_ERROR));
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
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        Order order = orderRepository.findByOrderIdAndUserId(id, user.getId()).orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_ERROR));
        order.setActive(Boolean.FALSE);
        orderRepository.save(order);
    }

    public void deleteAllOrdersForUser(String jws) {
        String username = extractUsername(jws);
        UserDto user = getUserByUsernameFromUserService(username);
        List<Order> orders = orderRepository.findAllByUserIdAndActive(user.getId(), Boolean.TRUE);
        orders.stream().forEach(order -> order.setActive(Boolean.FALSE));
        orderRepository.saveAll(orders);
    }

    private String extractUsername(String jws) {
        byte[] encodedSecret = Base64Utils.encode(jwtSecret.getBytes());
        Key key = new SecretKeySpec(encodedSecret, SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject();
    }

    private UserDto getUserByUsernameFromUserService(String username) {
        String urlString = "http://localhost:8091/api/users/search/email";
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(urlString);
        String urlTemplate = uriComponentsBuilder.queryParam("email", username).encode().toUriString();
        RestTemplate rest = new RestTemplate();
        ResponseEntity<UserDto> response = null;
        try {
            response = rest.exchange(urlTemplate, HttpMethod.GET, null, UserDto.class);
        } catch(RestClientException e) {
            throw new UserNotFoundException("Something went wrong while trying to retrieve user info");
        }
        UserDto user = null;
        if(response.getBody() != null) {
            user = response.getBody();
        }
        if(user == null) {
            throw new IllegalArgumentException("Something went wrong trying to find user");
        }
        return user;
    }

    private SecurityDto getSecurityByTypeAndId(SecurityType securityType, Long securityId) {
        StringBuilder sb = new StringBuilder("http://localhost:2000/api/data/");
        sb.append(securityType.toString().toLowerCase()).append(securityId);
        String urlString = sb.toString();
        RestTemplate rest = new RestTemplate();
        ResponseEntity<SecurityDto> response = null;
        try {
            response = rest.exchange(urlString, HttpMethod.GET, null, SecurityDto.class);
        } catch(RestClientException e) {
            throw new SecurityNotFoundException("Something went wrong while trying to retrieve user info");
        }
        SecurityDto security = null;
        if(response.getBody() != null) {
            security = response.getBody();
        }
        if (security == null) {
            throw new IllegalArgumentException("Something went wrong trying to find security");
        }
        return security;
    }

    private void executeLimitOrder(Order order, Integer amount, BigDecimal price, SecurityDto security, UserDto user, Long volume){
        BigDecimal cost;
        if(amount > 0 ) {
            order.setFee(formulaCalculator.calculateSecurityFee(order, security.getAsk(), price));
            cost = price.compareTo(security.getAsk()) > 1 ?
                    security.getAsk().multiply(BigDecimal.valueOf(amount)) :
                    price.multiply(BigDecimal.valueOf(amount));
        } else {
            order.setFee(formulaCalculator.calculateSecurityFee(order, security.getBid(), price));
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

    private void executeMarketOrder(Order order,Integer amount, SecurityDto security,UserDto user) {
        BigDecimal cost;
        if(amount > 0 ) {
            order.setFee(formulaCalculator.calculateSecurityFee(order, security.getAsk()));
            cost = security.getAsk().multiply(BigDecimal.valueOf(amount));
        } else {
            order.setFee(formulaCalculator.calculateSecurityFee(order, security.getBid()));
            cost = security.getBid().multiply(BigDecimal.valueOf(amount));
        }
        order.setCost(cost);
        order.setUserId(user.getId());
        order = orderRepository.save(order);
        long waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (security.getVolume() / Math.abs(amount))) * 1000L;
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
            if (orderFromRepo.getActive().booleanValue()) {
                Boolean allOrNone = order.getAllOrNone();
                if (allOrNone != null && allOrNone.booleanValue() && amountNotFilled != amountFilled && amountNotFilled > 0) {
                    long waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (volume / Math.abs(amountNotFilled))) * 1000L;

                    taskScheduler.schedule(new ExecuteOrderTask(amountNotFilled, order, volume), new Date(System.currentTimeMillis() + waitTime));
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
                        long waitTime = ThreadLocalRandom.current().nextLong(24 * 60 / (volume / Math.abs(amountNotFilled))) * 1000L;
                        taskScheduler.schedule(new ExecuteOrderTask(amountNotFilled, order, volume), new Date(System.currentTimeMillis() + waitTime));
                    }
                }
            }
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
