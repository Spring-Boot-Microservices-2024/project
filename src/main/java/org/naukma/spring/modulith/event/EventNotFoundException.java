package org.naukma.spring.modulith.event;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException() {
        super();
    }
    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public EventNotFoundException(String message) {
        super(message);
    }
    public EventNotFoundException(Long id) {
        super("Event not found with ID: "+id);
    }
    public EventNotFoundException(Throwable cause) {
        super(cause);
    }
}
