package ru.practicum.shareitserver.request.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareitserver.request.dto.CreateItemRequestDto;
import ru.practicum.shareitserver.request.model.ItemRequest;

public interface ItemRequestService {

    ItemRequest createItemRequest(CreateItemRequestDto dto);

    Optional<ItemRequest> getItemRequest(long itemRequestId);

    Collection<ItemRequest> getItemRequestsOfCurrentUser(int page, int size);

    Collection<ItemRequest> getAvailableRequestsForCurrentUser(int page, int size);
}
