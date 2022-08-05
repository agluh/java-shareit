package ru.practicum.shareit.request.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.security.AuthService;
import ru.practicum.shareit.user.model.User;

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
        service.getItemRequestsOfCurrentUser();

        // Then
        verify(repository, times(1)).findItemRequestsByRequester_IdOrderByCreatedAtDesc(userId);
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
            .findItemRequestsByRequester_IdIsNotOrderByCreatedAtDesc(userId, PageRequest.of(0, 10));
    }
}