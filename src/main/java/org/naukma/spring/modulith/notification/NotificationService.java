package org.naukma.spring.modulith.notification;

import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.order.OrderCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Order created: {}", event);
    }

    @Async
    @ApplicationModuleListener
    public void onOrderCreatedAsync(OrderCreatedEvent event) {
        log.info("Order created async: {}", event);
    }
}
