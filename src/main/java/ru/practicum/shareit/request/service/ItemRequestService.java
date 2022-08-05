package ru.practicum.shareit.request.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestService {

    ItemRequest createItemRequest(CreateItemRequestDto dto);

    Optional<ItemRequest> getItemRequest(long itemRequestId);

    Collection<ItemRequest> getItemRequestsOfCurrentUser();

    Collection<ItemRequest> getAvailableRequestsForCurrentUser(int page, int size);
}
