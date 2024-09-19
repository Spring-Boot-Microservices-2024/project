package org.naukma.spring.modulith.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.naukma.spring.modulith.event.EventDto;
import org.naukma.spring.modulith.user.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private UserDto author;
    private EventDto event;
    private int rating;
    private String comment;
}
