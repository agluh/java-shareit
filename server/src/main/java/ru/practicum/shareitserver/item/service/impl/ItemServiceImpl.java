package ru.practicum.shareitserver.item.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareitserver.item.dto.CreateItemDto;
import ru.practicum.shareitserver.item.dto.UpdateItemDto;
import ru.practicum.shareitserver.item.exception.ItemNotFoundException;
import ru.practicum.shareitserver.item.exception.NotAnOwnerException;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.item.repository.ItemRepository;
import ru.practicum.shareitserver.item.service.ItemService;
import ru.practicum.shareitserver.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareitserver.request.model.ItemRequest;
import ru.practicum.shareitserver.request.service.ItemRequestService;
import ru.practicum.shareitserver.security.AuthService;
import ru.practicum.shareitserver.user.model.User;

/**
 * Default implementation of ItemService.
 */
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final AuthService authService;
    private final ItemRequestService requestService;

    @Override
    public Item createItem(CreateItemDto dto) {
        User user = authService.getCurrentUser();

        ItemRequest request = null;
        if (dto.getRequestId() != null) {
            request = requestService.getItemRequest(dto.getRequestId())
                .orElseThrow(ItemRequestNotFoundException::new);
        }

        Item item = new Item(null, dto.getName(), dto.getDescription(), dto.getAvailable(),
            user, request);
        itemRepository.save(item);
        return item;
    }

    @Override
    public Item updateItem(UpdateItemDto dto) {
        Item item = itemRepository.findById(dto.getItemId())
            .orElseThrow(ItemNotFoundException::new);

        User user = authService.getCurrentUser();

        if (!item.getOwner().getId().equals(user.getId())) {
            throw new NotAnOwnerException();
        }

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }

        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }

        itemRepository.save(item);
        return item;
    }

    @Override
    public Optional<Item> getItem(long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public Collection<Item> getItemsOfCurrentUser(int from, int size) {
        User user = authService.getCurrentUser();
        int page = from / size;
        return itemRepository.findItemsByOwnerIdOrderByIdAsc(user.getId(),
            PageRequest.of(page, size));
    }

    @Override
    public Collection<Item> getItemsOfCurrentUser() {
        User user = authService.getCurrentUser();
        return itemRepository.findItemsByOwnerIdOrderByIdAsc(user.getId(), Pageable.unpaged());
    }

    @Override
    public Collection<Item> searchForItemsByNameAndDesc(String searchText, int from, int size) {
        if (searchText.isBlank()) {
            return Collections.emptyList();
        }
        int page = from / size;
        return itemRepository.searchByNameAndDesc(searchText, PageRequest.of(page, size)).stream()
            .filter(Item::isAvailable)
            .collect(Collectors.toList());
    }
}
