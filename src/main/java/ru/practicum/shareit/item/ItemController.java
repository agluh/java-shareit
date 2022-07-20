package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

/**
 * Controller of items.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;
    private final BookingService bookingService;
    private final AuthService authService;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody CreateItemDto dto) {
        Item item = itemService.createItem(dto);
        return ItemMapper.toDto(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
        @PathVariable("id") long itemId,
        @Valid @RequestBody UpdateItemDto dto
    ) {
        dto.setItemId(itemId);
        Item updated = itemService.updateItem(dto);
        return ItemMapper.toDto(updated);
    }

    @GetMapping("/{id}")
    public OwnerItemDto getItem(@PathVariable("id") long itemId) {
        Item item = itemService.getItem(itemId).orElseThrow(ItemNotFoundException::new);
        if (authService.isCurrentUserIdEqualsTo(item.getOwner().getId())) {
            LocalDateTime now = LocalDateTime.now();
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
    public Collection<OwnerItemDto> getItemsOfCurrentUser() {
        LocalDateTime now = LocalDateTime.now();
        return itemService.getItemsOfCurrentUser().stream()
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
        @PathVariable long itemId,
        @RequestBody @Valid CreateCommentDto dto
    ) {
        dto.setItemId(itemId);
        Comment comment = commentService.addComment(dto);
        return CommentMapper.toDto(comment);
    }
}
