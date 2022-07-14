package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            CommentMapper.toDto(item.getComments())
        );
    }

    public static Collection<ItemDto> toDto(Collection<Item> items) {
        return items.stream()
            .map(ItemMapper::toDto)
            .collect(Collectors.toList());
    }

    public static OwnerItemDto toOwnerDto(
        Item item,
        @Nullable Booking lastBooking,
        @Nullable Booking nextBooking
    ) {
        return new OwnerItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            CommentMapper.toDto(item.getComments()),
            lastBooking != null
                ? new OwnerItemDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId())
                : null,
            nextBooking != null
                ? new OwnerItemDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId())
                : null
        );
    }
}
