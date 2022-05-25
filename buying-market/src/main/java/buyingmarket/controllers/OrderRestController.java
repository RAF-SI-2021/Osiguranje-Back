package buyingmarket.controllers;

import buyingmarket.model.dto.OrderDto;
import buyingmarket.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<HttpStatus> createOrder(@RequestBody OrderDto orderDto, @RequestHeader("Authorization") String authorization) {
        orderService.createOrder(orderDto, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll(@RequestHeader("Authorization") String authorization) {
        List<OrderDto> orders = orderService.findAllOrdersForUser(authorization);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOrder(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        OrderDto order = orderService.findOrderForUser(id, authorization);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateOrder(@RequestBody OrderDto orderDto, @RequestHeader("Authorization") String authorization) {
        orderService.updateOrder(orderDto, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        orderService.deleteOrder(id, authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAll(@RequestHeader("Authorization") String authorization) {
        orderService.deleteAllOrdersForUser(authorization);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
