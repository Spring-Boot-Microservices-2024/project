package org.naukma.spring.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseBody
    public OrderEntity createOrder(@RequestBody OrderParams params) {
        return orderService.createOrder(params.getName(), params.getPrice());
    }

    @GetMapping
    @ResponseBody
    public List<OrderEntity> getAllOrders() {
        return orderService.getAllOrders();
    }
}
