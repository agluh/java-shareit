package ru.practicum.shareitgateway.item;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareitgateway.item.dto.CreateCommentDto;
import ru.practicum.shareitgateway.item.dto.CreateItemDto;
import ru.practicum.shareitgateway.item.dto.UpdateItemDto;
import ru.practicum.shareitgateway.security.UserId;

/**
 * Controller of items.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {

    private ItemClient client;

    @PostMapping
    public Object addItem(
        @UserId long userId,
        @Valid @RequestBody CreateItemDto dto
    ) {
        return client.addItem(userId, dto);
    }

    @PatchMapping("/{id}")
    public Object updateItem(
        @UserId long userId,
        @PathVariable("id") long itemId,
        @Valid @RequestBody UpdateItemDto dto
    ) {
        return client.updateItem(userId, itemId, dto);
    }

    @GetMapping("/{id}")
    public Object getItem(
        @UserId long userId,
        @PathVariable("id") long itemId
    ) {
        return client.getItem(userId, itemId);
    }

    @GetMapping
    public Object getItemsOfUser(
        @UserId long userId,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return client.getItemsOfUser(userId, from, size);
    }

    @GetMapping("/search")
    public Object searchItems(
        @RequestParam("text") String searchText,
        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
        @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {
        return client.searchItems(searchText, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public Object addComment(
        @UserId long userId,
        @PathVariable long itemId,
        @RequestBody @Valid CreateCommentDto dto
    ) {
        return client.addComment(userId, itemId, dto);
    }
}
