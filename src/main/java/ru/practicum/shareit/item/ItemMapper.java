package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.model.Item;

@Service
@RequiredArgsConstructor
public class ItemMapper {

    private final CommentMapper commentMapper;

    public ItemDto toDto(Item item) {
        return new ItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            item.getRequest() != null ? item.getRequest().getId() : null,
            commentMapper.toDto(item.getComments())
        );
    }

    public Collection<ItemDto> toDto(Collection<Item> items) {
        return items.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public OwnerItemDto toOwnerDto(
        Item item,
        @Nullable Booking lastBooking,
        @Nullable Booking nextBooking
    ) {
        return new OwnerItemDto(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.isAvailable(),
            item.getRequest() != null ? item.getRequest().getId() : null,
            commentMapper.toDto(item.getComments()),
            lastBooking != null
                ? new OwnerItemDto.Booking(lastBooking.getId(), lastBooking.getBooker().getId())
                : null,
            nextBooking != null
                ? new OwnerItemDto.Booking(nextBooking.getId(), nextBooking.getBooker().getId())
                : null
        );
    }
}
