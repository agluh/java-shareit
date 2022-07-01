package ru.practicum.shareit.item.service.impl;

import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NotAnOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.SecurityUser;

/**
 * Default implementation of ItemService.
 */
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item createItem(CreateItemDto dto) {
        SecurityUser user = getCurrentUser();
        Item item = new Item(null, dto.getName(), dto.getDescription(), dto.isAvailable(),
            user.getUserId(), null);
        itemRepository.save(item);
        return item;
    }

    @Override
    public Item updateItem(ItemDto dto) {
        Item item = itemRepository.getById(dto.getId()).orElseThrow(ItemNotFoundException::new);

        SecurityUser user = getCurrentUser();
        if (item.getOwnerId() != user.getUserId()) {
            throw new NotAnOwnerException();
        }

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.isAvailable());
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
        return itemRepository.searchByNameAndDesc(searchText);
    }

    private SecurityUser getCurrentUser() {
        return ((SecurityUser) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal());
    }
}
