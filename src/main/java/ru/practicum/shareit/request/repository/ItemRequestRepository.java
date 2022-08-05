package ru.practicum.shareit.request.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Collection<ItemRequest> findItemRequestsByRequester_IdOrderByCreatedAtDesc(long userId);

    List<ItemRequest> findItemRequestsByRequester_IdIsNotOrderByCreatedAtDesc(long userId,
        Pageable pageable);
}
