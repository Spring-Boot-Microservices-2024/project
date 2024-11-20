package org.naukma.spring.modulith.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.analytics.AnalyticsEvent;
import org.naukma.spring.modulith.analytics.AnalyticsEventType;
import org.naukma.spring.modulith.analytics.AnalyticsService;
import org.naukma.spring.modulith.booking.BookingException;
import org.naukma.spring.modulith.payment.PaymentGrpcClient;
import org.naukma.spring.modulith.payment.RefundData;
import org.naukma.spring.modulith.user.DeletedUserEvent;
import org.naukma.spring.modulith.user.UserDto;
import org.naukma.spring.modulith.user.UserMapper;
import org.naukma.spring.modulith.user.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AnalyticsService analyticsService;
    private final UserService userService;

    private final PaymentGrpcClient paymentGrpcClient;

    public List<EventDto> getAll() {
        return eventRepository.findAll().stream().map(EventMapper.INSTANCE::entityToDto).toList();
    }

    @Transactional
    public EventDto createEvent(CreateEventRequestDto event) {
        log.info("Creating event");
        UserDto organiser = userService.getUserById(event.getOrganiserId());
        EventEntity createdEvent = EventMapper.INSTANCE.createRequestDtoToEntity(event);
        createdEvent.setOrganiser(UserMapper.INSTANCE.dtoToEntity(organiser));
        createdEvent = eventRepository.save(createdEvent);
        eventPublisher.publishEvent(new AnalyticsEvent(AnalyticsEventType.EVENT_CREATED));
        analyticsService.reportEvent(AnalyticsEventType.EVENT_CREATED);
        log.info("Event created successfully.");
        return EventMapper.INSTANCE.entityToDto(createdEvent);
    }

    @Transactional
    public EventDto updateEvent(EventDto event) {
        EventEntity eventEntity = eventRepository.findById(event.getId())
                .orElseThrow(() -> new EventNotFoundException(event.getId()));
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


    public String deleteEvent(Long eventId) {
        Optional<EventEntity> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isPresent()) {
            EventEntity eventToDelete = eventOptional.get();
            eventRepository.deleteById(eventId);
            eventPublisher.publishEvent(new DeletedEventEvent(eventId));
            log.info("Deleted event with ID: {}", eventId);

            if (eventToDelete.getPrice() > 0 && !eventToDelete.getParticipants().isEmpty()) {
                returnPayment(EventMapper.INSTANCE.entityToDto(eventToDelete).getParticipants(), eventToDelete.getPrice());
                return "Event deleted successfully! Refund started, check logs for update";
            }

            return "Event deleted successfully!";
        } else {
            log.warn("Event not found for deletion with ID: {}", eventId);
            throw new EventNotFoundException();
        }
    }

    private void returnPayment(List<UserDto> participants, float refundSum) {
        List<RefundData> refunds = new ArrayList<>();

        for (UserDto user : participants) {
            long paymentId = new Random().nextLong();
            String timestamp = java.time.Instant.now().toString();

            refunds.add(new RefundData(paymentId, refundSum, timestamp));
            log.info("Prepared refund for user: {} with refund sum: {}", user.getId(), refundSum);
        }

        paymentGrpcClient.streamPaymentReturn(refunds);
    }

    public String addParticipant(Long eventId, UserDto user) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getParticipants().contains(UserMapper.INSTANCE.dtoToEntity(user))) {
            throw new BookingException("Cannot add participant. User is already registered for the event with ID: " + eventId);
        }
        event.getParticipants().add(UserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);

        if (event.getPrice() > 0) {
            String message = processPaymentForEvent(EventMapper.INSTANCE.entityToDto(event));
            return "Registration for event: " + message;
        }

        return "User registered for the event successfully!";
    }

    private String processPaymentForEvent(EventDto event) {
        long userId = event.getOrganiser().getId();
        long eventId = event.getId();
        float price = event.getPrice();
        String paymentMethod = "CreditCard";
        String timestamp = java.time.Instant.now().toString();

        String paymentResponse = paymentGrpcClient.processPayment(userId, eventId, price, paymentMethod, timestamp);
        log.info("Payment processing result for event {}: {}", eventId, paymentResponse);
        return paymentResponse;
    }

    public String removeParticipant(Long eventId, UserDto user) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        if (!event.getParticipants().contains(UserMapper.INSTANCE.dtoToEntity(user))) {
            throw new BookingException("Cannot remove participant. User is not registered for the event with ID: " + eventId);
        }
        event.getParticipants().remove(UserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);

        if (event.getPrice() > 0) {
            returnPayment(List.of(user), event.getPrice());
            return "User unregistered from the event successfully! Refund started, check logs for update";
        }

        return "User unregistered from the event successfully!";
    }

    public List<EventDto> findAllByOrganiserId(Long organiserId) {
        List<EventEntity> events = eventRepository.findAllByOrganiserId(organiserId);
        if (events.isEmpty()) {
            log.warn("No events found for organiser ID: {}", organiserId);
            return Collections.emptyList();
        }
        log.info("Retrieved {} events for organiser ID: {}", events.size(), organiserId);
        return events.stream().map(EventMapper.INSTANCE::entityToDto).toList();
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
        return events.stream().map(EventMapper.INSTANCE::entityToDto).toList();
    }

    public EventDto getEventById(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        log.info("Retrieved event with ID: {}", event.getId());
        return EventMapper.INSTANCE.entityToDto(event);
    }
}
