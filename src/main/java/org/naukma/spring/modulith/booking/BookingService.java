package org.naukma.spring.modulith.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.analytics.AnalyticsService;
import org.naukma.spring.modulith.event.EventService;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final EventService eventService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final AnalyticsService analyticsService;

    @Transactional
    public String registerUserForEvent(Long userId, Long eventId) {
        UserDto user = userService.getUserById(userId);
        String message = eventService.addParticipant(eventId, user);

        analyticsService.reportEvent(AnalyticsEventType.BOOKING_CREATED);
        log.info("SUCCESS: User with id {} registered for event {}", userId, eventId);
        eventPublisher.publishEvent(new RegisteredForEvent(eventId, userId));
        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.BOOKING_CREATED));

        return message;
    }

    @Transactional
    public String unregisterUserFromEvent(Long userId, Long eventId) {
        UserDto user = userService.getUserById(userId);
        String message = eventService.removeParticipant(eventId, user);

        log.info("SUCCESS: User with id {} unregistered from event {}", userId, eventId);
        eventPublisher.publishEvent(new UnregisteredFromEvent(eventId, userId));

        return message;
    }
}
