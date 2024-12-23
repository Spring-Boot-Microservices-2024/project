package org.naukma.spring.modulith.event;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.utils.ExceptionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@RequestBody @Valid CreateEventRequestDto event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        EventDto createdEvent = eventService.createEvent(event);
        log.info("Event created with ID: {}", createdEvent.getId());
        return createdEvent;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        log.info("Deleting event with ID: {}", id);
        String message = eventService.deleteEvent(id);
        log.info("Event deleted with ID: {}", id);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long eventId) {
        log.info("Retrieving event with ID: {}", eventId);
        return eventService.getEventById(eventId);
    }

    @GetMapping("/organiser/{id}")
    public List<EventDto> getAllByOrganiserId(@PathVariable Long id) {
        log.info("Getting all events by organiser id");
        return eventService.findAllByOrganiserId(id);
    }

    @GetMapping("/participant/{id}")
    public List<EventDto> getAllByParticipantId(@PathVariable Long id) {
        log.info("Getting all events by participant id");
        return eventService.findAllForParticipantById(id);
    }

    @GetMapping
    public List<EventDto> getAll() {
        log.info("Getting all events");
        return eventService.getAll();
    }

    @GetMapping("/period")
    public List<EventDto> getEventsForPeriod(@RequestParam("startDate") String startDate,
                                          @RequestParam("endDate") String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return eventService.getEventsForPeriod(start, end);
    }

    @GetMapping("/next-week")
    public List<EventDto> getEventsForNextWeek() {
        LocalDateTime startOfNextWeek = LocalDateTime.now()
                .with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.MONDAY));
        LocalDateTime endOfNextWeek = startOfNextWeek.plusDays(6);
        return eventService.getEventsForPeriod(startOfNextWeek, endOfNextWeek);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<String> handleEventNotFoundException(EventNotFoundException e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = ExceptionHelper.ERROR_PREFIX + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
