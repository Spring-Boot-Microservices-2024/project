package org.naukma.spring.modulith.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.naukma.spring.modulith.event.EventDto;
import org.naukma.spring.modulith.user.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEvent {
    private EventDto event;
    private UserDto user;
    private BookingEventType type;
}
