package ru.practicum.shareit.request.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.security.AuthService;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final AuthService authService;
    private final ItemRequestRepository repository;
    private final Clock clock;

    @Override
    public ItemRequest createItemRequest(CreateItemRequestDto dto) {
        User user = authService.getCurrentUser();
        LocalDateTime createdAt = LocalDateTime.now(clock);
        ItemRequest request = new ItemRequest(null, dto.getDescription(), user, createdAt);
        repository.save(request);
        return request;
    }

    @Override
    public Optional<ItemRequest> getItemRequest(long itemRequestId) {
        User user = authService.getCurrentUser();
        return repository.findById(itemRequestId);
    }

    @Override
    public Collection<ItemRequest> getItemRequestsOfCurrentUser() {
        User user = authService.getCurrentUser();
        return repository.findItemRequestsByRequester_IdOrderByCreatedAtDesc(user.getId());
    }

    @Override
    public Collection<ItemRequest> getAvailableRequestsForCurrentUser(int page, int size) {
        User user = authService.getCurrentUser();
        return repository.findItemRequestsByRequester_IdIsNotOrderByCreatedAtDesc(user.getId(),
            PageRequest.of(page, size));
    }
}
