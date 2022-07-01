package ru.practicum.shareit.user.service.impl;

import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(CreateUserDto dto) {
        User user = new User(null, dto.getName(), dto.getEmail());
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(UserDto dto) {
        User user = userRepository.getById(dto.getId()).orElseThrow(UserNotFoundException::new);
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> getUser(long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return userRepository.getAll();
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.getById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
