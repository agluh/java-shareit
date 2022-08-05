package ru.practicum.shareit.request;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

/**
 * Controller of item requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService service;
    private final ItemRequestMapper mapper;

    @PostMapping
    public ItemRequestDto addRequest(@RequestBody @Valid CreateItemRequestDto dto) {
        ItemRequest request = service.createItemRequest(dto);
        return mapper.toDto(request);
    }

    @GetMapping
    public Collection<ItemRequestDto> getRequestsOfCurrentUser() {
        return mapper.toDto(service.getItemRequestsOfCurrentUser());
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAvailableRequestsForCurrentUser(
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return mapper.toDto(service.getAvailableRequestsForCurrentUser(from, size));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@PathVariable("requestId") long requestId) {
        ItemRequest request = service.getItemRequest(requestId)
            .orElseThrow(ItemRequestNotFoundException::new);
        return mapper.toDto(request);
    }
}
