package ru.practicum.shareit.item.repository;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {

    void save(Item item);

    void delete(Item item);

    Optional<Item> getById(long itemId);

    Collection<Item> findByOwnerId(long userId);

    Collection<Item> searchByNameAndDesc(String text);
}
