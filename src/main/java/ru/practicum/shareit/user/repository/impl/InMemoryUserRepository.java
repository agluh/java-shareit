package ru.practicum.shareit.user.repository.impl;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailAddressIsNotUniqueException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Long, User> users;
    private static long nextId = 1;

    public InMemoryUserRepository() {
        users = new HashMap<>();
    }

    @Override
    public void save(User user) {
        ensureEmailIsUnique(user.getEmail(), user.getId());

        if (user.getId() == null) {
            injectId(user, getNextId());
        }

        users.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        if (user.getId() != null) {
            users.remove(user.getId());
        }
    }

    @Override
    public Optional<User> getById(long userId) {
        User user = users.get(userId);
        if (user == null) {
            return Optional.empty();
        }

        // Because we implement in-memory repo here we return a deep copy of an object
        // to avoid its pollution from outside of repository.
        return Optional.of(new User(user.getId(), user.getName(), user.getEmail()));
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    private void ensureEmailIsUnique(String email, Long userId) {
        boolean exists = users.values().stream()
            .filter(u -> !Objects.equals(u.getId(), userId))
            .map(User::getEmail)
            .anyMatch(e -> e.compareToIgnoreCase(email) == 0);

        if (exists) {
            throw new EmailAddressIsNotUniqueException();
        }
    }

    private static long getNextId() {
        return nextId++;
    }

    private void injectId(User user, long id) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
