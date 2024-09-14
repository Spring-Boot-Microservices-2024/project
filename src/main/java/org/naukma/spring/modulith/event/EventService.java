package org.naukma.spring.modulith.event;

import jakarta.persistence.EntityNotFoundException;
import org.naukma.spring.modulith.Application;
import org.naukma.spring.modulith.user.DeletedUserEvent;
import org.naukma.spring.modulith.user.IUserMapper;
import org.naukma.spring.modulith.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final IEventRepository eventRepository;
    static final Logger logger = LoggerFactory.getLogger(Application.class);

    public EventService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDto> getAll() {
        return eventRepository.findAll().stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public EventDto addEvent(EventDto event) {
        logger.info("Creating event");
        EventEntity createdEvent = eventRepository.save(IEventMapper.INSTANCE.dtoToEntity(event));
        logger.info("Event created successfully.");
        return IEventMapper.INSTANCE.entityToDto(createdEvent);
    }

    @Transactional
    public EventDto updateEvent(EventDto event) {
        Optional<EventEntity> optional = eventRepository.findById(event.getId());
        if (optional.isPresent()) {
            EventEntity eventEntity = optional.get();
            eventEntity.setCapacity(event.getCapacity());
            eventEntity.setDescription(event.getDescription());
            eventEntity.setCaption(event.getCaption());
            eventEntity.setAddress(event.getAddress());
            eventEntity.setDateTime(event.getDateTime());
            eventEntity.setPrice(event.getPrice());
            eventEntity.setOnline(event.isOnline());
            EventEntity editedEvent = eventRepository.save(eventEntity);
            logger.info("Updating event with id " + editedEvent.getId());
            return IEventMapper.INSTANCE.entityToDto(editedEvent);
        } else {
            throw new EntityNotFoundException("Event not found for editing");
        }
    }

    public void deleteEvent(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            Optional<EventEntity> event = eventRepository.findById(eventId);
            eventRepository.deleteById(eventId);
            logger.info("Deleted event with ID: {}", eventId);
        } else {
            logger.warn("Event not found for deletion with ID: {}", eventId);
        }
    }

    public void addParticipant(Long eventId, UserDto user){
        EventEntity event = eventRepository.findById(eventId).orElse(null);

        event.getParticipants().add(IUserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);
    }

    public void removeParticipant(Long eventId, UserDto user){
        EventEntity event = eventRepository.findById(eventId).orElse(null);

        event.getParticipants().remove(IUserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);
    }

    public List<EventDto> findAllByOrganiserId(Long organiserId) {
        List<EventEntity> events = eventRepository.findAllByOrganiserId(organiserId);
        if (events.isEmpty()) {
            logger.warn("No events found for organiser ID: {}", organiserId);
            return Collections.emptyList();
        }
        logger.info("Retrieved {} events for organiser ID: {}", events.size(), organiserId);
        return events.stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @EventListener
    public void onDeletedUserEvent(DeletedUserEvent event) {
        List<EventEntity> organisedEvents = eventRepository.findAllByOrganiserId(event.getUserId());
        eventRepository.deleteAll(organisedEvents);

        List<EventEntity> participationEvents = eventRepository.findAllByParticipantId(event.getUserId());
        for(EventEntity eventEntity :participationEvents){
            eventEntity.getParticipants().removeIf(user -> user.getId().equals(event.getUserId()));
            eventRepository.save(eventEntity);
        }
    }

    public List<EventDto> findAllForParticipantById(Long id) {
        List<EventEntity> events = eventRepository.findAllByParticipantId(id);
        if (events.isEmpty()) {
            logger.warn("No events found for participant ID: {}", id);
            return Collections.emptyList();
        }
        logger.info("Retrieved {} events for participant ID: {}", events.size(), id);
        return events.stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    public EventDto getEventById(Long eventId) {
        EventEntity event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            logger.info("Retrieved event with ID: {}", event.getId());
        } else {
            logger.warn("Event not found with ID: {}", eventId);
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }
        return IEventMapper.INSTANCE.entityToDto(event);
    }
}
