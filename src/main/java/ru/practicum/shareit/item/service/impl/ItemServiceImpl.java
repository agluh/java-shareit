package ru.practicum.shareit.item.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.NotAnOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
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
        User user = userService.getUser(dto.getUserId())
            .orElseThrow(ItemNotFoundException::new);

        Item item = new Item(null, dto.getName(), dto.getDescription(), dto.getAvailable(),
            user, null);
        itemRepository.save(item);
        return item;
    }

    @Override
    public Item updateItem(UpdateItemDto dto) {
        Item item = itemRepository.findById(dto.getItemId())
            .orElseThrow(ItemNotFoundException::new);

        User user = userService.getUser(dto.getUserId())
            .orElseThrow(UserNotFoundException::new);

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
    public Collection<Item> getItemsOfUser(long userId) {
        return itemRepository.findItemsByOwnerId(userId);
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
}
