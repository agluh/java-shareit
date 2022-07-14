package ru.practicum.shareit.security;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Component
@AllArgsConstructor
public class AuthService {

    private static final String USER_ID_HEADER = "x-sharer-user-id";

    private final UserService userService;

    public User getCurrentUser(Map<String, String> headers) {
        String userIdString = headers.get(USER_ID_HEADER);

        if (userIdString == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            long userId = Long.parseLong(userIdString);
            return userService.getUser(userId).orElseThrow(UserNotFoundException::new);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
