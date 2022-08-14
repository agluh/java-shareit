package ru.practicum.shareitserver.user.service;

import java.util.Collection;
import java.util.Optional;
import ru.practicum.shareitserver.user.dto.CreateUserDto;
import ru.practicum.shareitserver.user.dto.UpdateUserDto;
import ru.practicum.shareitserver.user.model.User;

public interface UserService {

    User createUser(CreateUserDto dto);

    User updateUser(long userId, UpdateUserDto dto);

    Optional<User> getUser(long userId);

    Collection<User> getUsers(int from, int size);

    void deleteUser(long userId);
}
