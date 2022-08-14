package ru.practicum.shareitserver.item.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareitserver.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByOwnerIdOrderByIdAsc(long userId, Pageable pageable);

    @Query(
        value = "SELECT * FROM items i "
        + "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) "
        + "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))",
        nativeQuery = true
    )
    List<Item> searchByNameAndDesc(String text, Pageable pageable);
}
