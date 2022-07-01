package ru.practicum.shareit.item.repository.impl;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

@Repository
public class ImMemoryItemRepository implements ItemRepository {

    private final HashMap<Long, Item> items;

    private static long nextId = 1;

    public ImMemoryItemRepository() {
        items = new HashMap<>();
    }

    @Override
    public void save(Item item) {
        if (item.getId() == null) {
            injectId(item, getNextId());
        }

        items.put(item.getId(), item);
    }

    @Override
    public void delete(Item item) {
        if (item.getId() != null) {
            items.remove(item.getId());
        }
    }

    @Override
    public Optional<Item> getById(long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            return Optional.empty();
        }

        // Because we implement in-memory repo here we return a deep copy of an object
        // to avoid its pollution from outside of repository.
        return Optional.of(new Item(item.getId(), item.getName(), item.getDescription(),
            item.isAvailable(), item.getOwnerId(), item.getRequestId()));
    }

    @Override
    public Collection<Item> findByOwnerId(long userId) {
        return items.values().stream()
            .filter(e -> e.getOwnerId() == userId)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchByNameAndDesc(String text) {
        Predicate<Item> inName = i -> i.getName().toUpperCase().contains(text.toUpperCase());
        Predicate<Item> inDesc = i -> i.getDescription().toUpperCase().contains(text.toUpperCase());

        return items.values().stream()
            .filter(inName.or(inDesc))
            .collect(Collectors.toList());
    }

    private static long getNextId() {
        return nextId++;
    }

    private void injectId(Item item, long id) {
        try {
            Field idField = Item.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(item, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
