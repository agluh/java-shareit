package ru.practicum.shareitserver.item.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareitserver.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.owner.id = ?1 ORDER BY i.id ASC")
    List<Item> findItemsOwnedByUser(long userId, Pageable pageable);

    @Query(
        "SELECT i FROM Item i "
        + "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) "
        + "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))"
    )
    List<Item> searchByNameAndDesc(String text, Pageable pageable);
}
