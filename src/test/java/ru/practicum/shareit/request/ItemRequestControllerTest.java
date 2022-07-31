package ru.practicum.shareit.request;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

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
    public void givenMalformedData_whenDoAddItemRequest_thenStatus400() throws Exception {
        String jsonContent = "{}";

        this.mockMvc
            .perform(post("/requests")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenCorrectRequest_whenDoGetItemRequestsOfCurrentUser_thenStatus200() throws Exception {
        when(service.getItemRequestsOfCurrentUser()).thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/requests"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenCorrectRequest_whenDoGetAvailableItemRequestsOfCurrentUser_thenStatus200() throws Exception {
        when(service.getAvailableRequestsForCurrentUser(anyInt(), anyInt())).thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/requests/all"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedRequest_whenDoGetAvailableItemRequestsOfCurrentUser_thenStatus400() throws Exception {
        this.mockMvc
            .perform(get("/requests/all")
                .param("from", "-1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
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
        when(service.getItemRequest(1)).thenReturn(Optional.of(Mockito.mock(ItemRequest.class)));

        this.mockMvc
            .perform(get("/requests/1"))
            .andExpect(status().isOk());
    }
}