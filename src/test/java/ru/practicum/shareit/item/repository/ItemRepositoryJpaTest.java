package ru.practicum.shareit.item.repository;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
class ItemRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository repository;

    @Test
    void givenStoredItem_whenFindItemsByOwner_thenCorrectListShouldBeReturned() {
        // Given
        User owner = new User(null, "User name", "email@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        em.persist(owner);
        em.persist(item);

        // When
        Collection<Item> result = repository.findItemsByOwnerIdOrderByIdAsc(owner.getId());

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(item));
    }

    @Test
    void givenStoredItem_whenSearchByName_thenCorrectListShouldBeReturned() {
        // Given
        User owner = new User(null, "User name", "email@example.com");
        Item item = new Item(null, "Item name", "Item description", true, owner, null);
        em.persist(owner);
        em.persist(item);

        // When
        Collection<Item> result = repository.searchByNameAndDesc("name");

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(item));
    }
}