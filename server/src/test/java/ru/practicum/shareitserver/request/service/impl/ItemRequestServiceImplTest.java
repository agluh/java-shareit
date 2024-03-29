package ru.practicum.shareitserver.request.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareitserver.request.dto.CreateItemRequestDto;
import ru.practicum.shareitserver.request.model.ItemRequest;
import ru.practicum.shareitserver.request.repository.ItemRequestRepository;
import ru.practicum.shareitserver.request.service.ItemRequestService;
import ru.practicum.shareitserver.security.AuthService;
import ru.practicum.shareitserver.user.model.User;

@SpringBootTest
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService service;

    @MockBean
    private ItemRequestRepository repository;

    @MockBean
    private AuthService authService;

    @Test
    void givenCorrectData_whenCreateItemRequest_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(authService.getCurrentUser()).thenReturn(user);

        CreateItemRequestDto dto = Mockito.mock(CreateItemRequestDto.class);
        when(dto.getDescription()).thenReturn("Description");

        // When
        ItemRequest request = service.createItemRequest(dto);

        // Then
        verify(repository, times(1)).save(request);
    }

    @Test
    void givenCorrectId_whenGetItemRequest_thenCorrectObjectShouldBeFetched() {
        // Given
        final long requestId = 1;

        User user = Mockito.mock(User.class);
        when(authService.getCurrentUser()).thenReturn(user);

        // When
        service.getItemRequest(requestId);

        // Then
        verify(repository, times(1)).findById(requestId);
    }

    @Test
    void givenCorrectUser_whenGetItemRequestsOfCurrentUser_thenCorrectObjectShouldBeFetched() {
        // Given
        final long userId = 1;

        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(userId);
        when(authService.getCurrentUser()).thenReturn(user);

        // When
        service.getItemRequestsOfCurrentUser(0, 10);

        // Then
        verify(repository, times(1))
            .findItemRequestsOfUser(userId, Pageable.ofSize(10));
    }

    @Test
    void givenCorrectUser_whenGetAvailableRequestsForCurrentUser_thenCorrectObjectShouldBeFetched() {
        // Given
        final long userId = 1;

        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(userId);
        when(authService.getCurrentUser()).thenReturn(user);

        // When
        service.getAvailableRequestsForCurrentUser(0, 10);

        // Then
        verify(repository, times(1))
            .findItemRequestsOfOtherUsers(userId, Pageable.ofSize(10));
    }
}