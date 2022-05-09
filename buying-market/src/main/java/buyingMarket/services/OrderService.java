package buyingMarket.services;

import app.model.dto.SecurityDTO;
import buyingMarket.mappers.OrderMapper;
import buyingMarket.model.Order;
import buyingMarket.model.Receipt;
import buyingMarket.model.dto.OrderCreateDto;
import buyingMarket.model.dto.OrderDto;
import buyingMarket.model.dto.ReceiptDto;
import buyingMarket.repositories.OrderRepository;
import buyingMarket.tasks.BuySecurityTask;
import crudApp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ReceiptService receiptService;
    private final TaskScheduler taskScheduler;
    private final RestTemplate serviceCommunicationRestTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ReceiptService receiptService, RestTemplate serviceCommunicationRestTemplate) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.receiptService = receiptService;
        this.taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(10));
        this.serviceCommunicationRestTemplate = serviceCommunicationRestTemplate;
    }

    public Optional<Order> findOrderById(long id) {
        return orderRepository.findById(id);
    }

    public OrderDto order(long userId, OrderCreateDto orderCreateDto, boolean buy, boolean sell) {
        Order order = orderRepository.save(orderMapper.orderCreateDtoToOrder(orderCreateDto));
        UserDto userDto;
        SecurityDTO securityDTO;
        try {
            ResponseEntity<UserDto> userResponseEntity = serviceCommunicationRestTemplate.exchange(
                    "http://localhost:8091/api/users/" + userId,
                    HttpMethod.GET,
                    null,
                    UserDto.class
            );
            userDto = userResponseEntity.getBody();
            ResponseEntity<SecurityDTO> seurityResponseEntity = serviceCommunicationRestTemplate.exchange(
                    "http://localhost:2000/api/data" + order.getSecurityId() + "?securityType=" + order.getSecurityType(),
                    HttpMethod.GET,
                    null,
                    SecurityDTO.class
            );
            securityDTO = seurityResponseEntity.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }
        if (userDto == null || securityDTO == null) {
            return null;
        }
        Optional<Receipt> optionalReceipt = receiptService.findReceiptByUser(userId);
        Receipt receipt;
        if (optionalReceipt.isEmpty()) {
            ReceiptDto receiptDto = new ReceiptDto(userId);
            receipt = receiptService.createReceipt(receiptDto);
        } else {
            receipt = optionalReceipt.get();
        }

        int transactionAmount = ThreadLocalRandom.current().nextInt(order.getAmount()) + 1;
        long transactionTime = ThreadLocalRandom.current().nextInt((int) (20 * 60 / (securityDTO.getVolume()) / order.getAmount())) * 1000L;
        taskScheduler.schedule(new BuySecurityTask(taskScheduler, receiptService, this, serviceCommunicationRestTemplate, receipt.getReceiptId(), order.getOrderId(), transactionAmount), new Date(System.currentTimeMillis() + transactionTime));
        return orderMapper.orderToOrderDto(order);
    }
}
