package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.AuthService;
import ru.practicum.shareit.user.model.User;

/**
 * Controller of items.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final AuthService authService;
    private final CommentService commentService;
    private final BookingService bookingService;

    @PostMapping
    public ItemDto addItem(
        @RequestHeader Map<String, String> headers,
        @Valid @RequestBody CreateItemDto dto
    ) {
        User user = authService.getCurrentUser(headers);
        dto.setUserId(user.getId());
        Item created = itemService.createItem(dto);
        return ItemMapper.toDto(created);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
        @RequestHeader Map<String, String> headers,
        @PathVariable("id") long itemId,
        @Valid @RequestBody UpdateItemDto dto
    ) {
        User user = authService.getCurrentUser(headers);
        dto.setUserId(user.getId());
        dto.setItemId(itemId);
        Item updated = itemService.updateItem(dto);
        return ItemMapper.toDto(updated);
    }

    @GetMapping("/{id}")
    public OwnerItemDto getItem(
        @RequestHeader Map<String, String> headers,
        @PathVariable("id") long itemId
    ) {
        User user = authService.getCurrentUser(headers);
        Item item = itemService.getItem(itemId).orElseThrow(ItemNotFoundException::new);
        LocalDateTime now = LocalDateTime.now();
        if (user.getId().equals(item.getOwner().getId())) {
            return ItemMapper.toOwnerDto(
                item,
                bookingService.getPreviousBookingOfItem(itemId, now),
                bookingService.getNextBookingOfItem(itemId, now)
            );
        } else {
            return ItemMapper.toOwnerDto(item, null, null);
        }
    }

    @GetMapping
    public Collection<OwnerItemDto> getItemsOfCurrentUser(
        @RequestHeader Map<String, String> headers
    ) {
        User user = authService.getCurrentUser(headers);
        LocalDateTime now = LocalDateTime.now();
        return itemService.getItemsOfUser(user.getId()).stream()
            .map(item -> ItemMapper.toOwnerDto(
                item,
                bookingService.getPreviousBookingOfItem(item.getId(), now),
                bookingService.getNextBookingOfItem(item.getId(), now)
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam("text") String searchText) {
        return ItemMapper.toDto(itemService.searchForItemsByNameAndDesc(searchText));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
        @RequestHeader Map<String, String> headers,
        @PathVariable long itemId,
        @RequestBody @Valid CreateCommentDto dto
    ) {
        User user = authService.getCurrentUser(headers);
        dto.setItemId(itemId);
        dto.setUserId(user.getId());
        Comment comment = commentService.addComment(dto);
        return CommentMapper.toDto(comment);
    }
}
