package buyingmarket.services;

import buyingmarket.exceptions.OrderNotFoundException;
import buyingmarket.exceptions.UpdateNotAllowedException;
import buyingmarket.formulas.FormulaCalculator;
import buyingmarket.mappers.OrderMapper;
import buyingmarket.model.*;
import buyingmarket.model.dto.OrderCreateDto;
import buyingmarket.model.dto.OrderDto;
import buyingmarket.model.dto.SecurityDto;
import buyingmarket.model.dto.TransactionDto;
import buyingmarket.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
    private TaskScheduler taskScheduler;
    private RestTemplate rest;
    private static final String ORDER_NOT_FOUND_ERROR = "No order with given id could be found for user";

    @Value("${api.securities}")
    private String securitiesApiUrl;

    @Value("${api.transaction}")
    private String transactionApiUrl;

    public OrderService() {}

    @Autowired
    public OrderService(ActuaryService actuaryService,
                        OrderRepository orderRepository,
                        OrderMapper orderMapper) {
        this.actuaryService = actuaryService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
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
                execute(order,jws);
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

        TransactionDto transactionDto = createTransaction(jws,order,"Creating init transaction for "+ order,0,0, FormulaCalculator.getEstimatedValue(order, security).intValue(),0);
        System.out.println(transactionDto);
        order.getTransactions().add(transactionDto.getId());
        orderRepository.save(order);
        if (order.getOrderState().equals(OrderState.APPROVED)) {
            execute(order,jws);
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
        List<Order> orders = orderRepository.findAllByActuary(actuary);
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

    protected TransactionDto createTransaction(String jwt,Order order,String text, int payment,int payout,int reserve, int usedReserve){
        String urlString = transactionApiUrl + "/api/transaction";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setOrderId(order.getOrderId());
        transactionDto.setCurrencyId(14L);
        transactionDto.setPayment(payment);
        transactionDto.setPayout(payout);
        transactionDto.setPayout(reserve);
        transactionDto.setUsedReserve(usedReserve);
        transactionDto.setText(text);
        transactionDto.setAccountId(1L);
        transactionDto.setUserId(actuaryService.getUserId(jwt));

        HttpEntity<?> entity = new HttpEntity<>(transactionDto,headers);

        ResponseEntity<?> response = rest.exchange(urlString,HttpMethod.POST,entity,Object.class);
        if(response.getStatusCode().is4xxClientError()){
            System.err.println(response.getBody());
            return null;
        }
        return (TransactionDto) response.getBody();

    }

    protected void execute(Order order,String jws) {
        SecurityDto security = getSecurityFromOrder(order);
        order.setFee(FormulaCalculator.calculateSecurityFee(order, security));
        order.setModificationDate(new Date());
        orderRepository.save(order);
        taskScheduler.schedule(new ExecuteOrderTask(order, order.getStopPrice() == null,jws), new Date(FormulaCalculator.waitTime(security.getVolume(), order.getAmount())));
    }




    public class ExecuteOrderTask implements Runnable {

        private Order order;
        private Boolean stopFlag;
        private String jws;

        public ExecuteOrderTask(Order order, Boolean stopFlag,String jws) {
            this.order = order;
            this.stopFlag = stopFlag;
            this.jws = jws;
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

                    TransactionDto transactionDto = createTransaction(jws,order,"Executing partial buyting",0,0,0,executeAmount);
                    order.getTransactions().add(transactionDto.getOrderId());
                    orderRepository.save(order);


                }
                taskScheduler.schedule(new ExecuteOrderTask(order, stopFlag,jws), new Date(FormulaCalculator.waitTime(security.getVolume(), order.getAmount())));
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
