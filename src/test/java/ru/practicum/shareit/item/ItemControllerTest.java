package ru.practicum.shareit.item;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.security.AuthService;
import ru.practicum.shareit.user.model.User;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private AuthService authService;

    @MockBean
    private ItemMapper itemMapper;

    @MockBean
    private CommentMapper commentMapper;

    @MockBean
    private Clock clock;

    @BeforeEach
    void setupClock() {
        when(clock.instant()).thenReturn(Instant.parse("2022-08-01T10:00:00.000Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }

    @Test
    public void givenCorrectData_whenDoAddItem_thenStatus200() throws Exception {
        String jsonContent = "{\"name\":\"Name\",\"description\":\"Description\",\"available\":true}";

        this.mockMvc
            .perform(post("/items")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedData_whenDoAddItem_thenStatus400() throws Exception {
        String jsonContent = "{\"name\":\"Name\"}";

        this.mockMvc
            .perform(post("/items")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenCorrectData_whenDoPatchItem_thenStatus200() throws Exception {
        String jsonContent = "{\"name\":\"Name\",\"description\":\"Description\",\"available\":true}";

        this.mockMvc
            .perform(patch("/items/1")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedData_whenDoPatchItem_thenStatus400() throws Exception {
        String jsonContent = "{}";

        this.mockMvc
            .perform(patch("/items/1")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenNotExistedItem_whenDoPatchItem_thenStatus404() throws Exception {
        String jsonContent = "{\"name\":\"Name\",\"description\":\"Description\",\"available\":true}";
        when(itemService.updateItem(any())).thenThrow(ItemNotFoundException.class);

        this.mockMvc
            .perform(patch("/items/1")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenExistedItem_whenDoGetItem_thenStatus200() throws Exception {
        Item item = Mockito.mock(Item.class);
        User user = Mockito.mock(User.class);
        when(item.getOwner()).thenReturn(user);
        when(itemService.getItem(1)).thenReturn(Optional.of(item));
        when(authService.isCurrentUserIdEqualsTo(1)).thenReturn(false);

        this.mockMvc
            .perform(get("/items/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenExistedItemAndOwner_whenDoGetItem_thenStatus200() throws Exception {
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(1L);

        Item item = Mockito.mock(Item.class);
        when(item.getOwner()).thenReturn(user);
        when(itemService.getItem(1)).thenReturn(Optional.of(item));
        when(authService.isCurrentUserIdEqualsTo(anyLong())).thenReturn(true);

        this.mockMvc
            .perform(get("/items/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenNotExistedItem_whenDoGetItem_thenStatus404() throws Exception {
        when(itemService.getItem(1)).thenReturn(Optional.empty());

        this.mockMvc
            .perform(get("/items/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenNoItems_whenDoGetItemsOfCurrentUser_thenStatus200() throws Exception {
        when(itemService.getItemsOfCurrentUser()).thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/items"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenExistedItems_whenDoGetItemsOfCurrentUser_thenStatus200() throws Exception {
        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(1L);
        when(itemService.getItemsOfCurrentUser()).thenReturn(List.of(item));

        this.mockMvc
            .perform(get("/items"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenValidRequest_whenDoSearchItems_thenStatus200() throws Exception {
        this.mockMvc
            .perform(get("/items/search")
                .param("text", "text"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedRequest_whenDoSearchItems_thenStatus400() throws Exception {
        this.mockMvc
            .perform(get("/items/search"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCorrectData_whenDoAddComment_thenStatus200() throws Exception {
        String jsonContent = "{\"itemId\":1,\"text\":\"Text\"}";

        this.mockMvc
            .perform(post("/items/1/comment")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedData_whenDoAddComment_thenStatus400() throws Exception {
        String jsonContent = "{\"itemId\":1}";

        this.mockMvc
            .perform(post("/items/1/comment")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }
}