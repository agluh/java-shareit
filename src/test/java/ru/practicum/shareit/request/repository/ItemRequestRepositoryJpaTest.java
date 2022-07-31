package ru.practicum.shareit.request.repository;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
class ItemRequestRepositoryJpaTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository repository;

    @Test
    void givenStoredItemRequest_whenFindItemRequestsByRequester_thenCorrectListShouldBeReturned() {
        // Given
        User requester = new User(null, "User name", "email@example.com");
        ItemRequest request = new ItemRequest(null,"Item description", requester, LocalDateTime.now());
        em.persist(requester);
        em.persist(request);

        // When
        Collection<ItemRequest> result = repository.findItemRequestsByRequester_IdOrderByCreatedAtDesc(requester.getId());

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(request));
    }

    @Test
    void givenStoredItemRequest_whenFindItemRequestsByNotRequester_thenCorrectListShouldBeReturned() {
        // Given
        User requester = new User(null, "User name", "email@example.com");
        User user = new User(null, "User name 2", "email2@example.com");
        ItemRequest request = new ItemRequest(null,"Item description", requester, LocalDateTime.now());
        em.persist(requester);
        em.persist(user);
        em.persist(request);

        // When
        Collection<ItemRequest> result = repository.findItemRequestsByRequester_IdIsNotOrderByCreatedAtDesc(user.getId(), Pageable.unpaged());

        // Then
        then(result).size().isEqualTo(1);
        then(result).containsExactlyElementsOf(List.of(request));
    }
}