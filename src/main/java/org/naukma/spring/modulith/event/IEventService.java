package org.naukma.spring.modulith.event;

import org.naukma.spring.modulith.user.DeletedUserEvent;
import org.naukma.spring.modulith.user.UserDto;

import java.util.List;

public interface IEventService {
    List<EventDto> getAll();
    EventDto createEvent(CreateEventRequestDto event);
    EventDto updateEvent(EventDto event);
    void deleteEvent(Long eventId);
    void addParticipant(Long eventId, UserDto user);
    void removeParticipant(Long eventId, UserDto user);
    List<EventDto> findAllByOrganiserId(Long organiserId);
    void onDeletedUserEvent(DeletedUserEvent event);
    List<EventDto> findAllForParticipantById(Long id);
    EventDto getEventById(Long eventId);
}
