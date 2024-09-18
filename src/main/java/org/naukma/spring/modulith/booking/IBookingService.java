package org.naukma.spring.modulith.booking;

public interface IBookingService {
    void registerUserForEvent(Long userId, Long eventId);
    void unregisterUserFromEvent(Long userId, Long eventId);
}
