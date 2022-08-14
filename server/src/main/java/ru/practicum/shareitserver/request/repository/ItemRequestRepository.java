package ru.practicum.shareitserver.request.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareitserver.request.model.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findItemRequestsByRequester_IdOrderByCreatedAtDesc(long userId,
        Pageable pageable);

    List<ItemRequest> findItemRequestsByRequester_IdIsNotOrderByCreatedAtDesc(long userId,
        Pageable pageable);
}
