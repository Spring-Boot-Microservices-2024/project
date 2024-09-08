package org.naukma.spring.modulith.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String name;
}
