package ru.practicum.shareitserver.request.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareitserver.request.model.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT r FROM ItemRequest r WHERE r.requester.id = ?1 ORDER BY r.createdAt DESC")
    List<ItemRequest> findItemRequestsOfUser(long userId, Pageable pageable);

    @Query("SELECT r FROM ItemRequest r WHERE r.requester.id <> ?1 ORDER BY r.createdAt DESC")
    List<ItemRequest> findItemRequestsOfOtherUsers(long userId, Pageable pageable);
}
