package ru.practicum.shareit.booking;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService service;

    @MockBean
    private BookingMapper mapper;

    @Test
    public void givenCorrectData_whenDoPostNewBooking_thenStatus200() throws Exception {
        String jsonContent = "{\"start\":\"2022-08-01T09:50:00\",\"end\":\"2022-08-02T09:50:00\",\"itemId\":1}";

        this.mockMvc
            .perform(post("/bookings")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedData_whenDoPostNewBooking_thenStatus400() throws Exception {
        String jsonContent = "{\"itemId\":1}";

        this.mockMvc
            .perform(post("/bookings")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenCorrectData_whenDoPostReviewBooking_thenStatus200() throws Exception {
        this.mockMvc
            .perform(patch("/bookings/1")
                .param("approved", "true"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenMalformedData_whenDoPostReviewBooking_thenStatus400() throws Exception {
        this.mockMvc
            .perform(patch("/bookings/1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenExistedBooking_whenDoGetBooking_thenStatus200() throws Exception {
        when(service.getBooking(1)).thenReturn(Optional.of(Mockito.mock(Booking.class)));

        this.mockMvc
            .perform(get("/bookings/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenNonExistedBooking_whenDoGetBooking_thenStatus404() throws Exception {
        when(service.getBooking(1)).thenReturn(Optional.empty());

        this.mockMvc
            .perform(get("/bookings/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void givenCorrectBookingState_whenDoGetBookings_thenStatus200() throws Exception {
        when(service.getBookingsOfCurrentUser(any())).thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/bookings"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenUnknownBookingState_whenDoGetBookings_thenStatus400() throws Exception {
        this.mockMvc
            .perform(get("/bookings")
                .param("state", "UNKNOWN"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    public void givenCorrectBookingState_whenDoGetBookingsOfOwner_thenStatus200() throws Exception {
        when(service.getBookingsOfCurrentUser(any())).thenReturn(Collections.emptyList());

        this.mockMvc
            .perform(get("/bookings/owner"))
            .andExpect(status().isOk());
    }

    @Test
    public void givenUnknownBookingState_whenDoGetBookingsOfOwner_thenStatus400() throws Exception {
        this.mockMvc
            .perform(get("/bookings/owner")
                .param("state", "UNKNOWN"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").isNotEmpty());
    }
}