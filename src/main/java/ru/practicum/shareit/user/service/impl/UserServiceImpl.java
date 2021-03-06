package ru.practicum.shareit.user.service.impl;

import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
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
    public User updateUser(long userId, UpdateUserDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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
        return userRepository.findById(userId);
    }

    @Override
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
