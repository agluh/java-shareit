package ru.practicum.shareitserver.item.service.impl;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareitserver.item.dto.CreateItemDto;
import ru.practicum.shareitserver.item.dto.UpdateItemDto;
import ru.practicum.shareitserver.item.exception.NotAnOwnerException;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.item.repository.ItemRepository;
import ru.practicum.shareitserver.item.service.ItemService;
import ru.practicum.shareitserver.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareitserver.request.model.ItemRequest;
import ru.practicum.shareitserver.request.service.ItemRequestService;
import ru.practicum.shareitserver.security.AuthService;
import ru.practicum.shareitserver.user.model.User;

@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRequestService requestService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private AuthService authService;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setupClock() {
        when(clock.instant()).thenReturn(Instant.parse("2022-08-01T10:00:00.000Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    void givenNoRequestPassed_whenCreateItem_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        CreateItemDto dto = Mockito.mock(CreateItemDto.class);
        when(dto.getName()).thenReturn("Name");
        when(dto.getDescription()).thenReturn("Description");
        when(dto.getAvailable()).thenReturn(true);
        when(dto.getRequestId()).thenReturn(null);

        // When
        Item item = itemService.createItem(dto);

        // Then
        then(item.getRequest()).isNull();
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void givenCorrectRequestPassed_whenCreateItem_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        CreateItemDto dto = Mockito.mock(CreateItemDto.class);
        when(dto.getName()).thenReturn("Name");
        when(dto.getDescription()).thenReturn("Description");
        when(dto.getAvailable()).thenReturn(true);
        when(dto.getRequestId()).thenReturn(1L);

        ItemRequest request = mock(ItemRequest.class);
        when(requestService.getItemRequest(1L)).thenReturn(Optional.of(request));

        // When
        Item item = itemService.createItem(dto);

        // Then
        then(item.getRequest()).isEqualTo(request);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void givenNonExistedRequestPassed_whenCreateItem_thenErrorShouldBeThrown() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        CreateItemDto dto = Mockito.mock(CreateItemDto.class);
        when(dto.getRequestId()).thenReturn(1L);

        when(requestService.getItemRequest(1L)).thenReturn(Optional.empty());

        // When
        final Throwable throwable = catchThrowable(() -> itemService.createItem(dto));

        // Then
        then(throwable).isInstanceOf(ItemRequestNotFoundException.class);
    }

    @Test
    void givenNonOwner_whenUpdateItem_thenErrorShouldBeThrown() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        User owner = Mockito.mock(User.class);
        when(user.getId()).thenReturn(2L);

        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getItemId()).thenReturn(1L);

        Item item = Mockito.mock(Item.class);
        when(item.getOwner()).thenReturn(owner);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // When
        final Throwable throwable = catchThrowable(() -> itemService.updateItem(dto));

        // Then
        then(throwable).isInstanceOf(NotAnOwnerException.class);
    }

    @Test
    void givenCurrentUserIsOwner_whenUpdateItem_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getItemId()).thenReturn(1L);
        when(dto.getName()).thenReturn("Name");
        when(dto.getDescription()).thenReturn("Description");
        when(dto.getAvailable()).thenReturn(true);

        Item item = Mockito.mock(Item.class);
        when(item.getOwner()).thenReturn(user);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // When
        itemService.updateItem(dto);

        // Then
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void givenCurrentUserIsOwnerAndPartialDto_whenUpdateItem_thenCorrectObjectShouldBeSaved() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        UpdateItemDto dto = Mockito.mock(UpdateItemDto.class);
        when(dto.getItemId()).thenReturn(1L);
        when(dto.getName()).thenReturn(null);
        when(dto.getDescription()).thenReturn(null);
        when(dto.getAvailable()).thenReturn(null);

        Item item = Mockito.mock(Item.class);
        when(item.getOwner()).thenReturn(user);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // When
        itemService.updateItem(dto);

        // Then
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void givenItemId_whenGetItem_thenCorrectObjectShouldBeFetched() {
        // Given
        final long itemId = 1;

        // When
        itemService.getItem(itemId);

        // Then
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void givenCurrentUser_whenGetItemsOfCurrentUser_thenCorrectObjectsShouldBeFetched() {
        // Given
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        // When
        itemService.getItemsOfCurrentUser(0, 10);

        // Then
        verify(itemRepository, times(1)).findItemsByOwnerIdOrderByIdAsc(1L,
            Pageable.ofSize(10));
    }

    @Test
    void givenEmptySearchRequest_whenSearchForItemsByNameAndDesc_thenEmptyListShouldBeFetched() {
        // Given
        final String text = "";

        // When
        Collection<Item> result = itemService.searchForItemsByNameAndDesc(text, 0, 10);

        // Then
        then(result).isEmpty();
        verifyNoInteractions(itemRepository);
    }

    @Test
    void givenSearchRequest_whenSearchForItemsByNameAndDesc_thenListShouldBeFetched() {
        // Given
        final String text = "non empty";

        // When
        itemService.searchForItemsByNameAndDesc(text, 0, 10);

        // Then
        verify(itemRepository, times(1)).searchByNameAndDesc(text,
            Pageable.ofSize(10));
    }
}