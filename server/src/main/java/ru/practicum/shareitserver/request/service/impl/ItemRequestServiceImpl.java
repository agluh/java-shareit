package ru.practicum.shareitserver.request.service.impl;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareitserver.request.dto.CreateItemRequestDto;
import ru.practicum.shareitserver.request.model.ItemRequest;
import ru.practicum.shareitserver.request.repository.ItemRequestRepository;
import ru.practicum.shareitserver.request.service.ItemRequestService;
import ru.practicum.shareitserver.security.AuthService;
import ru.practicum.shareitserver.user.model.User;

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
    public Collection<ItemRequest> getItemRequestsOfCurrentUser(int from, int size) {
        User user = authService.getCurrentUser();
        int page = from / size;
        return repository.findItemRequestsOfUser(user.getId(),
            PageRequest.of(page, size));
    }

    @Override
    public Collection<ItemRequest> getAvailableRequestsForCurrentUser(int from, int size) {
        User user = authService.getCurrentUser();
        int page = from / size;
        return repository.findItemRequestsOfOtherUsers(user.getId(),
            PageRequest.of(page, size));
    }
}
