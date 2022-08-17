package ru.practicum.shareitserver.item.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareitserver.item.dto.CreateItemDto;
import ru.practicum.shareitserver.item.dto.UpdateItemDto;
import ru.practicum.shareitserver.item.exception.NotAnOwnerException;
import ru.practicum.shareitserver.item.model.Item;

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
     * Gets all items of a user, paged.
     */
    Collection<Item> getItemsOfCurrentUser(int from, int size);

    /**
     * Gets all items of a user.
     */
    Collection<Item> getItemsOfCurrentUser();

    /**
     * Searches items by names and descriptions.
     */
    Collection<Item> searchForItemsByNameAndDesc(String searchText, int from, int size);
}
