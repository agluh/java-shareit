package ru.practicum.shareitserver.security;

import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareitserver.user.exception.UserNotFoundException;
import ru.practicum.shareitserver.user.model.User;
import ru.practicum.shareitserver.user.service.UserService;

@Component
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@AllArgsConstructor
public class AuthService {

    private static final String USER_ID_HEADER = "x-sharer-user-id";

    private final HttpServletRequest request;
    private final UserService userService;

    public User getCurrentUser() {
        String userIdString = request.getHeader(USER_ID_HEADER);

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

    public boolean isCurrentUserIdEqualsTo(long userId) {
        return getCurrentUser().getId().equals(userId);
    }
}
