package ru.practicum.shareitserver.user.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareitserver.user.dto.CreateUserDto;
import ru.practicum.shareitserver.user.dto.UpdateUserDto;
import ru.practicum.shareitserver.user.model.User;
import ru.practicum.shareitserver.user.repository.UserRepository;
import ru.practicum.shareitserver.user.service.UserService;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    void givenCorrectData_whenCreateUser_thenCorrectObjectShouldBeSaved() {
        // Given
        CreateUserDto dto = Mockito.mock(CreateUserDto.class);

        // When
        User user = service.createUser(dto);

        // Then
        verify(repository, times(1)).save(user);
    }

    @Test
    void givenExistedUser_whenUpdateUser_thenCorrectObjectShouldBeSaved() {
        // Given
        UpdateUserDto dto = Mockito.mock(UpdateUserDto.class);

        User user = Mockito.mock(User.class);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // When
        service.updateUser(1L, dto);

        // Then
        verify(repository, times(1)).save(user);
    }

    @Test
    void givenExistedUser_whenGetUser_thenCorrectObjectShouldBeFetched() {
        // Given
        final long userId = 1;

        // When
        service.getUser(userId);

        // Then
        verify(repository, times(1)).findById(userId);
    }

    @Test
    void givenCorrectData_whenGetUsers_thenCorrectObjectsShouldBeFetched() {
        // Given
        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        // When
        service.getUsers(0, 10);

        // Then
        verify(repository, times(1)).findAll(Pageable.ofSize(10));
    }

    @Test
    void givenExistedUser_whenDeleteUser_thenCorrectObjectShouldBeDeleted() {
        // Given
        User user = Mockito.mock(User.class);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // When
        service.deleteUser(1L);

        // Then
        verify(repository, times(1)).delete(user);
    }
}