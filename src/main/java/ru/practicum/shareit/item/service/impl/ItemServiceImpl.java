package ru.practicum.shareit.item.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NotAnOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.SecurityUser;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

/**
 * Default implementation of ItemService.
 */
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public Item createItem(CreateItemDto dto) {
        SecurityUser user = getCurrentUser();
        userService.getUser(user.getUserId()).orElseThrow(UserNotFoundException::new);
        Item item = new Item(null, dto.getName(), dto.getDescription(), dto.getAvailable(),
            user.getUserId(), null);
        itemRepository.save(item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, UpdateItemDto dto) {
        Item item = itemRepository.getById(itemId).orElseThrow(ItemNotFoundException::new);

        SecurityUser user = getCurrentUser();
        if (item.getOwnerId() != user.getUserId()) {
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
        return itemRepository.getById(itemId);
    }

    @Override
    public Collection<Item> getItemsOfUser(long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public Collection<Item> searchForItemsByNameAndDesc(String searchText) {
        if (searchText.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchByNameAndDesc(searchText).stream()
            .filter(Item::isAvailable)
            .collect(Collectors.toList());
    }

    private SecurityUser getCurrentUser() {
        try {
            return (SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        } catch (ClassCastException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
