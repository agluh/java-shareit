package ru.practicum.shareitserver.item;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareitserver.booking.service.BookingService;
import ru.practicum.shareitserver.item.dto.CommentDto;
import ru.practicum.shareitserver.item.dto.CreateCommentDto;
import ru.practicum.shareitserver.item.dto.CreateItemDto;
import ru.practicum.shareitserver.item.dto.ItemDto;
import ru.practicum.shareitserver.item.dto.OwnerItemDto;
import ru.practicum.shareitserver.item.dto.UpdateItemDto;
import ru.practicum.shareitserver.item.exception.ItemNotFoundException;
import ru.practicum.shareitserver.item.model.Comment;
import ru.practicum.shareitserver.item.model.Item;
import ru.practicum.shareitserver.item.service.CommentService;
import ru.practicum.shareitserver.item.service.ItemService;
import ru.practicum.shareitserver.security.AuthService;

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
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @PostMapping
    public ItemDto addItem(@RequestBody CreateItemDto dto) {
        Item item = itemService.createItem(dto);
        return itemMapper.toDto(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
        @PathVariable("id") long itemId,
        @RequestBody UpdateItemDto dto
    ) {
        dto.setItemId(itemId);
        Item updated = itemService.updateItem(dto);
        return itemMapper.toDto(updated);
    }

    @GetMapping("/{id}")
    public OwnerItemDto getItem(@PathVariable("id") long itemId) {
        Item item = itemService.getItem(itemId).orElseThrow(ItemNotFoundException::new);
        if (authService.isCurrentUserIdEqualsTo(item.getOwner().getId())) {
            return itemMapper.toOwnerDto(
                item,
                bookingService.getPreviousBookingOfItem(itemId),
                bookingService.getNextBookingOfItem(itemId)
            );
        } else {
            return itemMapper.toOwnerDto(item, null, null);
        }
    }

    @GetMapping
    public Collection<OwnerItemDto> getItemsOfCurrentUser(
        @RequestParam(name = "from", defaultValue = "0") int from,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return itemService.getItemsOfCurrentUser(from, size).stream()
            .map(item -> itemMapper.toOwnerDto(
                item,
                bookingService.getPreviousBookingOfItem(item.getId()),
                bookingService.getNextBookingOfItem(item.getId())
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(
        @RequestParam("text") String searchText,
        @RequestParam(name = "from", defaultValue = "0") int from,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return itemMapper.toDto(itemService.searchForItemsByNameAndDesc(searchText, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
        @PathVariable long itemId,
        @RequestBody CreateCommentDto dto
    ) {
        dto.setItemId(itemId);
        Comment comment = commentService.addComment(dto);
        return commentMapper.toDto(comment);
    }
}
