package ru.practicum.shareitserver.request;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareitserver.request.model.ItemRequest;
import ru.practicum.shareitserver.request.service.ItemRequestService;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService service;

    @MockBean
    private ItemRequestMapper mapper;

    @Test
    public void givenCorrectData_whenDoAddItemRequest_thenStatus200() throws Exception {
        String jsonContent = "{\"description\":\"Description\"}";

        this.mockMvc
            .perform(post("/requests")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenCorrectRequest_whenDoGetItemRequestsOfCurrentUser_thenStatus200()
            throws Exception {
        when(service.getItemRequestsOfCurrentUser(0, 10))
            .thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/requests"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenCorrectRequest_whenDoGetAvailableItemRequestsOfCurrentUser_thenStatus200()
            throws Exception {
        when(service.getAvailableRequestsForCurrentUser(anyInt(), anyInt()))
            .thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/requests/all"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenNotExistedItemRequest_whenDoGetItemRequest_thenStatus404() throws Exception {
        when(service.getItemRequest(1)).thenReturn(Optional.empty());

        this.mockMvc
            .perform(get("/requests/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenExistedItemRequest_whenDoGetItemRequest_thenStatus200() throws Exception {
        when(service.getItemRequest(1))
            .thenReturn(Optional.of(Mockito.mock(ItemRequest.class)));

        this.mockMvc
            .perform(get("/requests/1"))
            .andExpect(status().isOk());
    }
}