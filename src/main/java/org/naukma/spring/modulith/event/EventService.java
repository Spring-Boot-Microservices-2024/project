package org.naukma.spring.modulith.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.analytics.AnalyticsService;
import org.naukma.spring.modulith.user.DeletedUserEvent;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AnalyticsService analyticsService;

    public List<EventDto> getAll() {
        return eventRepository.findAll().stream().map(EventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public EventDto createEvent(CreateEventRequestDto event) {
        log.info("Creating event");
        EventEntity createdEvent = eventRepository.save(EventMapper.INSTANCE.createRequestDtoToEntity(event));
        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.EVENT_CREATED));
        analyticsService.reportEvent(AnalyticsEventType.EVENT_CREATED);
        log.info("Event created successfully.");
        return EventMapper.INSTANCE.entityToDto(createdEvent);
    }

    @Transactional
    public EventDto updateEvent(EventDto event) {
        EventEntity eventEntity = eventRepository.findById(event.getId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + event.getId()));
        eventEntity.setCapacity(event.getCapacity());
        eventEntity.setDescription(event.getDescription());
        eventEntity.setCaption(event.getCaption());
        eventEntity.setAddress(event.getAddress());
        eventEntity.setDateTime(event.getDateTime());
        eventEntity.setPrice(event.getPrice());
        eventEntity.setOnline(event.isOnline());
        EventEntity editedEvent = eventRepository.save(eventEntity);
        log.info("Updating event with id {}", editedEvent.getId());
        return EventMapper.INSTANCE.entityToDto(editedEvent);
    }


    public void deleteEvent(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            eventPublisher.publishEvent(new DeletedEventEvent(eventId));
            log.info("Deleted event with ID: {}", eventId);
        } else {
            log.warn("Event not found for deletion with ID: {}", eventId);
        }
    }


    public void addParticipant(Long eventId, UserDto user) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
        event.getParticipants().add(UserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);
    }


    public void removeParticipant(Long eventId, UserDto user) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
        event.getParticipants().remove(UserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);
    }


    public List<EventDto> findAllByOrganiserId(Long organiserId) {
        List<EventEntity> events = eventRepository.findAllByOrganiserId(organiserId);
        if (events.isEmpty()) {
            log.warn("No events found for organiser ID: {}", organiserId);
            return Collections.emptyList();
        }
        log.info("Retrieved {} events for organiser ID: {}", events.size(), organiserId);
        return events.stream().map(EventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @EventListener
    public void onDeletedUserEvent(DeletedUserEvent event) {
        List<EventEntity> organisedEvents = eventRepository.findAllByOrganiserId(event.getUserId());
        eventRepository.deleteAll(organisedEvents);
        List<EventEntity> participationEvents = eventRepository.findAllByParticipantId(event.getUserId());
        for (EventEntity eventEntity : participationEvents) {
            eventEntity.getParticipants().removeIf(user -> user.getId().equals(event.getUserId()));
            eventRepository.save(eventEntity);
        }
    }

    public List<EventDto> findAllForParticipantById(Long id) {
        List<EventEntity> events = eventRepository.findAllByParticipantId(id);
        if (events.isEmpty()) {
            log.warn("No events found for participant ID: {}", id);
            return Collections.emptyList();
        }
        log.info("Retrieved {} events for participant ID: {}", events.size(), id);
        return events.stream().map(EventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    public EventDto getEventById(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
        log.info("Retrieved event with ID: {}", event.getId());
        return EventMapper.INSTANCE.entityToDto(event);
    }
}
