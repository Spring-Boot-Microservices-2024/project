package org.naukma.spring.modulith.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BookingException extends RuntimeException {

    public BookingException() {
        super();
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookingException(String message) {
        super(message);
    }

    public BookingException(Throwable cause) {
        super(cause);
    }
}

