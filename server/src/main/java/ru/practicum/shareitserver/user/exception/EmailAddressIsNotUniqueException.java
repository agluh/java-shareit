package ru.practicum.shareitserver.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAddressIsNotUniqueException extends RuntimeException {

    public EmailAddressIsNotUniqueException(Throwable cause) {
        super(cause);
    }
}
