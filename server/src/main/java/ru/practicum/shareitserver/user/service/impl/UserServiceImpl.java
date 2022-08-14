package ru.practicum.shareitserver.user.service.impl;

import java.util.Collection;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareitserver.user.dto.CreateUserDto;
import ru.practicum.shareitserver.user.dto.UpdateUserDto;
import ru.practicum.shareitserver.user.exception.EmailAddressIsNotUniqueException;
import ru.practicum.shareitserver.user.exception.UserNotFoundException;
import ru.practicum.shareitserver.user.model.User;
import ru.practicum.shareitserver.user.repository.UserRepository;
import ru.practicum.shareitserver.user.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(CreateUserDto dto) {
        try {
            User user = new User(null, dto.getName(), dto.getEmail());
            userRepository.save(user);
            return user;
        } catch (DataIntegrityViolationException e) {
            throw new EmailAddressIsNotUniqueException(e);
        }
    }

    @Override
    public User updateUser(long userId, UpdateUserDto dto) {
        try {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            if (dto.getName() != null) {
                user.setName(dto.getName());
            }
            if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            }
            userRepository.save(user);
            return user;
        } catch (DataIntegrityViolationException e) {
            throw new EmailAddressIsNotUniqueException(e);
        }
    }

    @Override
    public Optional<User> getUser(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Collection<User> getUsers(int from, int size) {
        int page = from / size;
        return userRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
