package ru.practicum.shareit.request;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Service
public class ItemRequestMapper {

    public ItemRequestDto toDto(ItemRequest request) {
        return new ItemRequestDto(
            request.getId(),
            request.getDescription(),
            request.getCreatedAt(),
            request.getItems().stream()
                .map(e -> new ItemRequestDto.Item(
                    e.getId(),
                    e.getName(),
                    e.getDescription(),
                    e.isAvailable(),
                    request.getRequester().getId()
                ))
                .collect(Collectors.toList())
        );
    }

    public Collection<ItemRequestDto> toDto(Collection<ItemRequest> requests) {
        return requests.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
