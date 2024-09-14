package org.naukma.spring.modulith.notification;

import lombok.extern.slf4j.Slf4j;
import org.naukma.spring.modulith.booking.RegisteredForEvent;
import org.naukma.spring.modulith.booking.UnregisteredFromEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationService {
    @ApplicationModuleListener
    public void onRegisteredForEventAsync(RegisteredForEvent event) {
        log.info("RegisteredForEvent async: {}", event);
    }

    @ApplicationModuleListener
    public void onUnregisteredFromEventAsync(UnregisteredFromEvent event) {
        log.info("UnregisteredFromEvent async: {}", event);
    }
}
