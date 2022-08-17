package ru.practicum.shareitserver.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotAnOwnerException extends RuntimeException {

}
