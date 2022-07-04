package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * Controller for users.
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto registerUser(@Valid @RequestBody CreateUserDto dto) {
        User created = userService.createUser(dto);
        return UserMapper.toDto(created);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(
        @PathVariable("id") long userId,
        @Valid @RequestBody UpdateUserDto dto
    ) {
        User updated = userService.updateUser(userId, dto);
        return UserMapper.toDto(updated);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") long userId) {
        User user = userService.getUser(userId).orElseThrow(UserNotFoundException::new);
        return UserMapper.toDto(user);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers().stream()
            .map(UserMapper::toDto)
            .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long userId) {
        userService.deleteUser(userId);
    }
}
