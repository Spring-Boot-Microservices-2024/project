package org.naukma.spring.modulith.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/new")
    public EventRequestDto createEvent(@RequestBody @Valid EventRequestDto event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        EventDto createdEvent = eventService.addEvent(IEventMapper.INSTANCE.requestDtoToDto(event));
        logger.info("Event created with ID: {}", createdEvent.getId());
        return IEventMapper.INSTANCE.dtoToRequestDto(createdEvent);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        logger.info("Deleting event with ID: {}", id);
        eventService.deleteEvent(id);
        logger.info("Event deleted with ID: {}", id);
    }

    @GetMapping("/{eventId}")
    public EventRequestDto getEventById(@PathVariable Long eventId) {
        logger.info("Retrieving event with ID: {}", eventId);
        return IEventMapper.INSTANCE.dtoToRequestDto(eventService.getEventById(eventId));
    }

    @GetMapping("/organiser/{id}")
    public List<EventRequestDto> getAllByOrganiserId(@PathVariable Long id){
        logger.info("Getting all events by organiser id");
        return eventService.findAllByOrganiserId(id).stream().map(IEventMapper.INSTANCE::dtoToRequestDto).collect(Collectors.toList());
    }
    @GetMapping("/participant/{id}")
    public List<EventRequestDto> getAllByParticipantId(@PathVariable Long id){
        logger.info("Getting all events by participant id");
        return eventService.findAllForParticipantById(id).stream().map(IEventMapper.INSTANCE::dtoToRequestDto).collect(Collectors.toList());
    }

    @GetMapping
    public List<EventRequestDto> getAll(){
        logger.info("Getting all events");
        return eventService.getAll().stream().map(IEventMapper.INSTANCE::dtoToRequestDto).collect(Collectors.toList());
    }
}
