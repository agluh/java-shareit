package ru.practicum.shareitserver.request.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemRequestNotFoundException extends RuntimeException {

}
