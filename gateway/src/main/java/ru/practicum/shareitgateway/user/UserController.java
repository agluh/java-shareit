package ru.practicum.shareitgateway.user;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareitgateway.user.dto.CreateUserDto;
import ru.practicum.shareitgateway.user.dto.UpdateUserDto;

/**
 * Controller for users.
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
public class UserController {

    private UserClient client;

    @PostMapping
    public Object registerUser(@Valid @RequestBody CreateUserDto dto) {
        return client.registerUser(dto);
    }

    @PatchMapping("/{id}")
    public Object updateUser(
        @PathVariable("id") long userId,
        @Valid @RequestBody UpdateUserDto dto
    ) {
        return client.updateUser(userId, dto);
    }

    @GetMapping("/{id}")
    public Object getUser(@PathVariable("id") long userId) {
        return client.getUser(userId);
    }

    @GetMapping
    public Object getUsers(
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return client.getUsers(from, size);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long userId) {
        client.deleteUser(userId);
    }
}
