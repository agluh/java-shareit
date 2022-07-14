package ru.practicum.shareit.item.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exception.NotAnOwnerException;
import ru.practicum.shareit.item.model.Item;

/**
 * Contains methods for items management.
 */
public interface ItemService {

    /**
     * Creates a new item.
     */
    Item createItem(CreateItemDto dto);

    /**
     * Updates an existing item.
     *
     * @throws NotAnOwnerException if current user is not an item owner
     */
    Item updateItem(UpdateItemDto dto);

    /**
     * Gets item by its identity.
     */
    Optional<Item> getItem(long itemId);

    /**
     * Gets all items of a user.
     */
    Collection<Item> getItemsOfUser(long userId);

    /**
     * Searches items by names and descriptions.
     */
    Collection<Item> searchForItemsByNameAndDesc(String searchText);
}
