package org.naukma.spring.modulith.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.naukma.spring.modulith.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private Long id;

    @NotBlank
    @Size(max = 100, message = "Event caption length must be less than 100 characters")
    private String caption;

    private LocalDateTime dateTime;

    @Min(value = 0, message = "Event price must be 0 or more")
    private float price;

    @EqualsAndHashCode.Exclude
    private List<UserDto> participants;

    @NotBlank
    private String description;

    private boolean online;

    @Min(value = 1, message = "Event capacity must be 1 or more")
    private int capacity;

    @NotBlank
    private String address;

    @NotNull
    private UserDto organiser;
}
