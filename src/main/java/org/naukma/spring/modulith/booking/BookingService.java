package org.naukma.spring.modulith.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.analytics.AnalyticsService;
import org.naukma.spring.modulith.event.EventDto;
import org.naukma.spring.modulith.event.EventService;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jms.core.JmsTemplate;
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
    private final JmsTemplate jmsTemplate;

    @Transactional
    public void registerUserForEvent(Long userId, Long eventId) {
        UserDto user = userService.getUserById(userId);
        EventDto event = eventService.getEventById(eventId);
        eventService.addParticipant(eventId, user);
        analyticsService.reportEvent(AnalyticsEventType.BOOKING_CREATED);
        sendBookingEventToJMS(new BookingEvent(event, user, BookingEventType.USER_REGISTERED_FOR_EVENT));
        log.info("SUCCESS: User with id {} registered for event {}", userId, eventId);
        eventPublisher.publishEvent(new RegisteredForEvent(eventId, userId));
        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.BOOKING_CREATED));
    }

    @Transactional
    public void unregisterUserFromEvent(Long userId, Long eventId) {
        UserDto user = userService.getUserById(userId);
        EventDto event = eventService.getEventById(eventId);
        eventService.removeParticipant(eventId, user);
        sendBookingEventToJMS(new BookingEvent(event, user, BookingEventType.USER_UNREGISTERED_FROM_EVENT));
        log.info("SUCCESS: User with id {} unregistered from event {}", userId, eventId);
        eventPublisher.publishEvent(new UnregisteredFromEvent(eventId, userId));
    }

    private void sendBookingEventToJMS(BookingEvent bookingEvent) {
        jmsTemplate.setPubSubDomain(true);
        String jmsTopicName = "booking";
        jmsTemplate.convertAndSend(jmsTopicName, bookingEvent);
    }
}
