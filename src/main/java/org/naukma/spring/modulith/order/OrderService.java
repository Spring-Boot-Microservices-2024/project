package org.naukma.spring.modulith.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderEntity createOrder(String name, double price) {
        var order = new OrderEntity();
        order.setName(name);
        order.setPrice(price);
        repository.save(order);
        eventPublisher.publishEvent(new OrderCreatedEvent(order.getId(), order.getName()));
        return order;
    }

    public List<OrderEntity> getAllOrders() {
        return repository.findAll();
    }
}
