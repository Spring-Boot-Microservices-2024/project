package org.naukma.spring.modulith.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.event.EventNotFoundException;
import org.naukma.spring.modulith.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PutMapping("/{eventId}/register/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUserForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        bookingService.registerUserForEvent(userId, eventId);
        return ResponseEntity.ok("User registered for the event successfully.");
    }

    @PutMapping("/{eventId}/unregister/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> unregisterUserFromEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        bookingService.unregisterUserFromEvent(userId, eventId);
        return ResponseEntity.ok("User unregistered from the event successfully.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> handleEventNotFoundException(EventNotFoundException e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
