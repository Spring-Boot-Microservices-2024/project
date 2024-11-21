package org.naukma.spring.modulith.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.event.EventNotFoundException;
import org.naukma.spring.modulith.user.UserNotFoundException;
import org.naukma.spring.modulith.utils.ExceptionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PutMapping("/{eventId}/register/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUserForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        String message = bookingService.registerUserForEvent(userId, eventId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{eventId}/unregister/{userId}")
    public ResponseEntity<String> unregisterUserFromEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        String message = bookingService.unregisterUserFromEvent(userId, eventId);
        return ResponseEntity.ok(message);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<String> handleBookingException(BookingException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> handleEventNotFoundException(EventNotFoundException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
