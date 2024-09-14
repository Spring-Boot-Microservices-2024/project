package org.naukma.spring.modulith.event;

import lombok.*;
import org.naukma.spring.modulith.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String caption;
    private LocalDateTime dateTime;
    private float price;
    @EqualsAndHashCode.Exclude
    private List<UserDto> participants;
    private String description;
    private boolean online;
    private int capacity;
    private String address;
    private UserDto organiser;
}
