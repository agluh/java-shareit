package ru.practicum.shareit.item.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findItemsByOwnerIdOrderByIdAsc(long userId);

    @Query(
        "SELECT i FROM Item i " +
        "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
        "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))"
    )
    Collection<Item> searchByNameAndDesc(String text);
}
