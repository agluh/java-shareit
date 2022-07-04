package ru.practicum.shareit.user.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    User createUser(CreateUserDto dto);

    User updateUser(long userId, UpdateUserDto dto);

    Optional<User> getUser(long userId);

    Collection<User> getUsers();

    void deleteUser(long userId);
}
