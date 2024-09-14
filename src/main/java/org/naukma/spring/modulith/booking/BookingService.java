package org.naukma.spring.modulith.booking;

import lombok.RequiredArgsConstructor;
import org.naukma.spring.modulith.Application;
import org.naukma.spring.modulith.event.EventService;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final EventService eventService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Transactional
    public void registerUserForEvent(Long userId, Long eventId){
        UserDto user = userService.getUserById(userId);
        eventService.addParticipant(eventId, user);
        logger.info("SUCCESS: User with id {} registered for event {}", userId,eventId);

        eventPublisher.publishEvent(new RegisteredForEvent(eventId, userId));
    }

    @Transactional
    public void unregisterUserFromEvent(Long userId, Long eventId){
        UserDto user = userService.getUserById(userId);
        eventService.removeParticipant(eventId, user);
        logger.info("SUCCESS: User with id {} unregistered from event {}", userId,eventId);

        eventPublisher.publishEvent(new UnregisteredFromEvent(eventId, userId));
    }
}
