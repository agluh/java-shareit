package ru.practicum.shareitgateway.request;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareitgateway.request.dto.CreateItemRequestDto;
import ru.practicum.shareitgateway.security.UserId;

/**
 * Controller of item requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {

    private RequestClient client;

    @PostMapping
    public Object addRequest(
        @UserId long userId,
        @RequestBody @Valid CreateItemRequestDto dto
    ) {
        return client.addRequest(userId, dto);
    }

    @GetMapping
    public Object getRequestsOfCurrentUser(
        @UserId long userId,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return client.getRequestsOfCurrentUser(userId, from, size);
    }

    @GetMapping("/all")
    public Object getAvailableRequestsForCurrentUser(
        @UserId long userId,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return client.getAvailableRequestsForCurrentUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Object getRequest(
        @UserId long userId,
        @PathVariable("requestId") long requestId
    ) {
        return client.getRequest(userId, requestId);
    }
}
