package buyingMarket.tasks;

import app.model.dto.SecurityDTO;
import buyingMarket.mappers.OrderMapper;
import buyingMarket.model.Order;
import buyingMarket.model.OrderType;
import buyingMarket.model.Receipt;
import buyingMarket.model.dto.TransactionDto;
import buyingMarket.services.OrderService;
import buyingMarket.services.ReceiptService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class BuySecurityTask implements Runnable {
    private TaskScheduler taskScheduler;
    private ReceiptService receiptService;
    private OrderService orderService;
    private RestTemplate serviceCommunicationRestTemplate;
    private long receiptId;
    private long orderId;
    private int purchased;
    private BigDecimal value;
    private boolean buy;
    private boolean sell;

    public BuySecurityTask(TaskScheduler taskScheduler, ReceiptService receiptService, OrderService orderService, RestTemplate serviceCommunicationRestTemplate, long receiptId, long orderId, int purchased, BigDecimal value, boolean buy, boolean sell) {
        this.taskScheduler = taskScheduler;
        this.receiptService = receiptService;
        this.orderService = orderService;
        this.serviceCommunicationRestTemplate = serviceCommunicationRestTemplate;
        this.receiptId = receiptId;
        this.orderId = orderId;
        this.purchased = purchased;
        this.value = value;
        this.buy = buy;
        this.sell = sell;
    }

    @Override
    public void run() {
        Optional<Order> orderOptional = orderService.findOrderById(orderId);
        if (orderOptional.isEmpty()) {
            return;
        }
        Order order = orderOptional.get();
        SecurityDTO securityDTO;
        try {
            ResponseEntity<SecurityDTO> securityResponseEntity = serviceCommunicationRestTemplate.exchange(
                    "http://localhost:2000/api/data/" + order.getSecurityType() + '/' + order.getSecurityId(),
                    HttpMethod.GET,
                    null,
                    SecurityDTO.class
            );
            securityDTO = securityResponseEntity.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return;
        }
        if (securityDTO == null) {
            return;
        }
        TransactionDto transactionDto = new TransactionDto(
                receiptId,
                LocalDateTime.now(),
                securityDTO.getName(),
                securityDTO.getPrice(),
                securityDTO.getVolume()
        );
        receiptService.addTransaction(receiptId, transactionDto);
        Optional<Receipt> receiptOptional = receiptService.findReceiptById(receiptId);
        if (receiptOptional.isEmpty()) {
            return;
        }
        Receipt receipt = receiptOptional.get();
//        if (purchased < order.getAmount()) {
//            int transactionAmount = ThreadLocalRandom.current().nextInt(order.getAmount() - purchased) + 1;
//            long transactionTime = ThreadLocalRandom.current().nextInt((int) (20 * 60 / (securityDTO.getVolume()) / order.getAmount()) - purchased) * 1000L;
//            taskScheduler.schedule(new BuySecurityTask(taskScheduler, receiptService, orderService, serviceCommunicationRestTemplate, receiptId, order.getOrderId(), transactionAmount + purchased), new Date(System.currentTimeMillis() + transactionTime));
//        }
        formatTask(receipt.getUserId(), taskScheduler, receiptService, orderService, serviceCommunicationRestTemplate, securityDTO, value, receiptId, orderId, purchased, buy, sell);
    }

    public static void formatTask(long userId, TaskScheduler taskScheduler, ReceiptService receiptService, OrderService orderService, RestTemplate serviceCommunicationRestTemplate, SecurityDTO securityDTO, BigDecimal value, long receiptId , long orderId, int purchased, boolean buy, boolean sell){
        Optional<Order> orderOptional = orderService.findOrderById(orderId);
        if (orderOptional.isEmpty()) {
            return;
        }
        Order order = orderOptional.get();
        int transactionAmount = 0;
        long transactionTime = 0;

        OrderType orderType = order.getOrderType();
        boolean scheduleTask = false;
        switch (orderType){
            case STOP:
                if((buy && securityDTO.getAsk().compareTo(value) > 0) ||
                        (sell && securityDTO.getBid().compareTo(value) > 0)){
                order.setOrderType(OrderType.MARKET);
                orderService.order(userId, (new OrderMapper()).orderToOrderCreateDto(order), buy, sell, new BigDecimal(0));
                }
                break;
            case LIMIT:
                break;
            case MARKET:
                if (purchased < order.getAmount()) {
                    transactionAmount = ThreadLocalRandom.current().nextInt(order.getAmount() - purchased) + 1;
                    transactionTime = ThreadLocalRandom.current().nextInt((int) (20 * 60 / (securityDTO.getVolume()) / order.getAmount()) - purchased) * 1000L;
                    scheduleTask = true;
                }
                break;
            case STOP_LIMIT:
                break;
        }

        if(order.isAllOrNone()){
            transactionAmount = order.getAmount();
        }

        if(scheduleTask){
            taskScheduler.schedule(new BuySecurityTask(taskScheduler, receiptService, orderService, serviceCommunicationRestTemplate, receiptId, order.getOrderId(), transactionAmount + purchased, value, buy, sell), new Date(System.currentTimeMillis() + transactionTime));
        }

    }
}
