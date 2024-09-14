package org.naukma.spring.modulith.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisteredForEvent {
    private Long eventId;
    private Long userId;
}
