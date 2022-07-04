package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.SecurityUser;

/**
 * Controller of items.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody CreateItemDto dto) {
        Item created = itemService.createItem(dto);
        return ItemMapper.toDto(created);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
        @PathVariable("id") long itemId,
        @Valid @RequestBody UpdateItemDto dto
    ) {
        Item updated = itemService.updateItem(itemId, dto);
        return ItemMapper.toDto(updated);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable("id") long itemId) {
        Item item = itemService.getItem(itemId).orElseThrow(ItemNotFoundException::new);
        return ItemMapper.toDto(item);
    }

    @GetMapping
    public Collection<ItemDto> getItems(@AuthenticationPrincipal SecurityUser user) {
        return itemService.getItemsOfUser(user.getUserId()).stream()
            .map(ItemMapper::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam("text") String searchText) {
        return itemService.searchForItemsByNameAndDesc(searchText).stream()
            .map(ItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
