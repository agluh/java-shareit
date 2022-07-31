package ru.practicum.shareit.user;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper mapper;

    @Test
    public void givenCorrectData_whenDoAddUser_thenStatus200() throws Exception {
        String jsonContent = "{\"name\":\"Name\", \"email\":\"email@example.com\"}";

        this.mockMvc
            .perform(post("/users")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedData_whenDoAddUser_thenStatus400() throws Exception {
        String jsonContent = "{\"name\":\"Name\", \"email\":\"not a email\"}";

        this.mockMvc
            .perform(post("/users")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenCorrectData_whenDoPatchUser_thenStatus200() throws Exception {
        String jsonContent = "{\"name\":\"Name\", \"email\":\"email@example.com\"}";

        this.mockMvc
            .perform(patch("/users/1")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedData_whenDoPatchUser_thenStatus400() throws Exception {
        String jsonContent = "{}";

        this.mockMvc
            .perform(patch("/users/1")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenNotExistedUser_whenDoGetUser_thenStatus404() throws Exception {
        when(userService.getUser(1)).thenReturn(Optional.empty());

        this.mockMvc
            .perform(get("/users/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenExistedItem_whenDoGetUser_thenStatus404() throws Exception {
        when(userService.getUser(1)).thenReturn(Optional.of(Mockito.mock(User.class)));

        this.mockMvc
            .perform(get("/users/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenCorrectRequest_whenDoGetUsers_thenStatus200() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/users"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenExistedUser_whenDoDeleteUser_thenStatus200() throws Exception {
        this.mockMvc
            .perform(delete("/users/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenNonExistedUser_whenDoDeleteUser_thenStatus404() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).deleteUser(1);

        this.mockMvc
            .perform(delete("/users/1"))
            .andExpect(status().isNotFound());
    }
}