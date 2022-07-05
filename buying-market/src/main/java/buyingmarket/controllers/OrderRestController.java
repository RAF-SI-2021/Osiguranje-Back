package buyingmarket.controllers;

import buyingmarket.services.OrderService;
import dto.market.OrderCreateDto;
import dto.market.OrderDto;
import dto.market.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto, @RequestHeader("Authorization") String authorization) {
        try {
            orderService.createOrder(orderCreateDto, authorization);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll(@RequestHeader("Authorization") String authorization) {
        List<OrderDto> orders = orderService.findAllOrdersForUser(authorization);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOrder(@NotNull @PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        OrderDto order = orderService.findOrderForUser(id, authorization);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@NotNull @PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        orderService.deleteOrder(id, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAll(@RequestHeader("Authorization") String authorization) {
        orderService.deleteAllOrdersForUser(authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> validateOrder(@PathVariable Long orderId, @RequestBody OrderState orderState, @RequestHeader("Authorization") String authorization) {
        orderService.validateOrder(orderId, orderState, authorization);
        return ResponseEntity.noContent().build();
    }
}
