package org.naukma.spring.modulith.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.naukma.spring.modulith.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
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
