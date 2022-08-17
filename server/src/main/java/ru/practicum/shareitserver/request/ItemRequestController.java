package ru.practicum.shareitserver.request;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareitserver.request.dto.CreateItemRequestDto;
import ru.practicum.shareitserver.request.dto.ItemRequestDto;
import ru.practicum.shareitserver.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareitserver.request.model.ItemRequest;
import ru.practicum.shareitserver.request.service.ItemRequestService;

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
    public ItemRequestDto addRequest(@RequestBody CreateItemRequestDto dto) {
        ItemRequest request = service.createItemRequest(dto);
        return mapper.toDto(request);
    }

    @GetMapping
    public Collection<ItemRequestDto> getRequestsOfCurrentUser(
        @RequestParam(name = "from", defaultValue = "0") int from,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return mapper.toDto(service.getItemRequestsOfCurrentUser(from, size));
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAvailableRequestsForCurrentUser(
        @RequestParam(name = "from", defaultValue = "0") int from,
        @RequestParam(name = "size", defaultValue = "10") int size
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
