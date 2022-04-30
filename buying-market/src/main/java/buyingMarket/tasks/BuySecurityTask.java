package buyingMarket.tasks;

import app.model.dto.SecurityDTO;
import buyingMarket.model.Order;
import buyingMarket.model.dto.TransactionDto;
import buyingMarket.services.OrderService;
import buyingMarket.services.ReceiptService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    public BuySecurityTask(TaskScheduler taskScheduler, ReceiptService receiptService, OrderService orderService, RestTemplate serviceCommunicationRestTemplate, long receiptId , long orderId, int purchased) {
        this.taskScheduler = taskScheduler;
        this.receiptService = receiptService;
        this.orderService = orderService;
        this.serviceCommunicationRestTemplate = serviceCommunicationRestTemplate;
        this.receiptId = receiptId;
        this.orderId = orderId;
        this.purchased = purchased;
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
            ResponseEntity<SecurityDTO> seurityResponseEntity = serviceCommunicationRestTemplate.exchange(
                    "http://localhost:2000/api/data" + order.getSecurityId() + "?securityType=" + order.getSecurityType(),
                    HttpMethod.GET,
                    null,
                    SecurityDTO.class
            );
            securityDTO = seurityResponseEntity.getBody();
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
        if (purchased < order.getAmount()) {
            int transactionAmount = ThreadLocalRandom.current().nextInt(order.getAmount() - purchased) + 1;
            long transactionTime = ThreadLocalRandom.current().nextInt((int) (20 * 60 / (securityDTO.getVolume()) / order.getAmount()) - purchased) * 1000L;
            taskScheduler.schedule(new BuySecurityTask(taskScheduler, receiptService, orderService, serviceCommunicationRestTemplate, receiptId, order.getOrderId(), transactionAmount + purchased), new Date(System.currentTimeMillis() + transactionTime));
        }
    }
}
