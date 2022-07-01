package ru.practicum.shareit.user.repository;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareit.user.exception.EmailAddressIsNotUniqueException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    /**
     * @throws EmailAddressIsNotUniqueException if email is not unique
     */
    void save(User user);

    void delete(User user);

    Optional<User> getById(long userId);

    Collection<User> getAll();
}
