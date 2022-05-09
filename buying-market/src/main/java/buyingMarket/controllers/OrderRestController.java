package buyingMarket.controllers;

import buyingMarket.model.dto.OrderCreateDto;
import buyingMarket.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {
    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    @PostMapping(value = "/order/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> order(@PathVariable long userId, @RequestBody OrderCreateDto order, @RequestParam boolean buy, @RequestParam boolean sell) {
        if (buy && sell || !buy && !sell) {
            return ResponseEntity.status(400).build();
        }
        orderService.order(userId, order, buy, sell);
        return ResponseEntity.ok().build();
    }
}
