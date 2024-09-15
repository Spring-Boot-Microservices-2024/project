package org.naukma.spring.modulith.review.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.naukma.spring.modulith.event.EventDto;
import org.naukma.spring.modulith.user.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {

    @NotNull
    private UserDto author;

    @NotNull
    private EventDto event;

    @Min(value = 1, message = "Review rating must be between 1 and 10")
    @Max(value = 10, message = "Review rating must be between 1 and 10")
    private int rating;

    @Size(max = 300, message = "Review comment length must be less than 300 characters")
    @NotNull
    @NotBlank
    private String comment;
}
