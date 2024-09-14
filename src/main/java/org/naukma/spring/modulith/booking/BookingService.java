package org.naukma.spring.modulith.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.event.EventService;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final EventService eventService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void registerUserForEvent(Long userId, Long eventId) {
        Optional<UserDto> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            log.error("User with id {} not found", userId);
            return;
        }

        eventService.addParticipant(eventId, user.get());
        log.info("SUCCESS: User with id {} registered for event {}", userId, eventId);

        eventPublisher.publishEvent(new RegisteredForEvent(eventId, userId));
        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.BOOKING_CREATED));
    }

    @Transactional
    public void unregisterUserFromEvent(Long userId, Long eventId) {
        Optional<UserDto> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            log.error("User with id {} not found", userId);
            return;
        }

        eventService.removeParticipant(eventId, user.get());
        log.info("SUCCESS: User with id {} unregistered from event {}", userId, eventId);

        eventPublisher.publishEvent(new UnregisteredFromEvent(eventId, userId));
    }
}
