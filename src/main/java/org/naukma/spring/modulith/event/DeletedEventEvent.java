package org.naukma.spring.modulith.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedEventEvent {
    private Long eventId;
}
