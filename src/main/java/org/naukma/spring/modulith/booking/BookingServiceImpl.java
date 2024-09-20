package org.naukma.spring.modulith.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.event.EventServiceImpl;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl {

    private final EventServiceImpl eventService;

    private final UserServiceImpl userService;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void registerUserForEvent(Long userId, Long eventId) {
        UserDto user = userService.getUserById(userId);
        eventService.addParticipant(eventId, user);
        log.info("SUCCESS: User with id {} registered for event {}", userId, eventId);
        eventPublisher.publishEvent(new RegisteredForEvent(eventId, userId));
        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.BOOKING_CREATED));
    }

    @Transactional
    public void unregisterUserFromEvent(Long userId, Long eventId) {
        UserDto user = userService.getUserById(userId);
        eventService.removeParticipant(eventId, user);
        log.info("SUCCESS: User with id {} unregistered from event {}", userId, eventId);
        eventPublisher.publishEvent(new UnregisteredFromEvent(eventId, userId));
    }
}
