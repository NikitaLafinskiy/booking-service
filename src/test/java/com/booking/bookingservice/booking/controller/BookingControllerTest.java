package com.booking.bookingservice.booking.controller;

import static com.booking.bookingservice.utils.BookingTestUtils.setUpExistantBookingDto;
import static com.booking.bookingservice.utils.BookingTestUtils.setUpExistantCreateBookingRequestDto;
import static com.booking.bookingservice.utils.BookingTestUtils.setUpNonExistantBookingDto;
import static com.booking.bookingservice.utils.BookingTestUtils.setUpNonExistantCreateBookingRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.booking.bookingservice.config.WithJwtMockUser;
import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.CreateBookingRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Sql(scripts = {"classpath:scripts/clear-tables.sql"})
@Sql(scripts = {"classpath:scripts/user/insert-user.sql",
        "classpath:scripts/accommodation/insert-2-accommodations.sql",
        "classpath:scripts/booking/insert-2-bookings.sql"})
public class BookingControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("""
            Given a valid request body
            When POST request is sent to /bookings
            Then return a Booking DTO
            """)
    @WithJwtMockUser
    void createBooking_validRequestBody_returnBookingDto() throws Exception {
        // Given
        CreateBookingRequestDto createBookingRequestDto = setUpNonExistantCreateBookingRequestDto();
        BookingDto expected = setUpNonExistantBookingDto();
        String jsonRequest = objectMapper.writeValueAsString(createBookingRequestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookingDto actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                BookingDto.class);
        boolean isEqual = EqualsBuilder.reflectionEquals(expected,
                actual,
                "id");
        assertTrue(isEqual);
    }

    @Test
    @DisplayName("""
            Given an invalid request body
            When POST request is sent to /bookings
            Then return a 400 Bad Request status
            """)
    @WithJwtMockUser(isAdmin = true)
    void createBooking_invalidRequestBody_returnBadRequestStatus() throws Exception {
        // Given
        CreateBookingRequestDto createBookingRequestDto = new CreateBookingRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createBookingRequestDto);

        // When
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            Given a valid request body with overlapping dates
            When POST request is sent to /bookings
            Then return a 400 Bad Request status
            """)
    @WithJwtMockUser
    void createBooking_overlappingDates_returnBadRequestStatus() throws Exception {
        // Given
        CreateBookingRequestDto createBookingRequestDto = setUpExistantCreateBookingRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createBookingRequestDto);

        // When
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("""
            Given a userId and a status
            When a get request is made to /bookings
            Then return a list of Booking DTOs
            """)
    @WithJwtMockUser(isAdmin = true)
    void getUserBookings_validRequestParams_returnBookingDtos() throws Exception {
        // Given
        List<BookingDto> expected = List.of(setUpExistantBookingDto());

        // When
        MvcResult mvcResult = mockMvc.perform(get(
                "/bookings?userId=2&status=CONFIRMED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookingDto> actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(EqualsBuilder.reflectionEquals(expected.get(i),
                    actual.get(i),
                    "id"));
        }
    }

    @Test
    @DisplayName("""
            Given a userId and an invalid status
            When a get request is made to /bookings
            Then return a 400 Bad Request status
            """)
    @WithJwtMockUser(isAdmin = true)
    void getUserBookings_invalidStatus_returnBadRequestStatus() throws Exception {
        // When
        mockMvc.perform(get(
                        "/bookings?userId=2&status=INVALID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("""
            When a get request is made to /bookings/my
            Then return a list of Booking DTOs
            """)
    @WithJwtMockUser
    void getMyBookings_returnBookingDtos() throws Exception {
        // Given
        List<BookingDto> expected = List.of(setUpExistantBookingDto());

        // When
        MvcResult mvcResult = mockMvc.perform(get("/bookings/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookingDto> actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(EqualsBuilder.reflectionEquals(expected.get(i),
                    actual.get(i),
                    "id"));
        }
    }

    @Test
    @DisplayName("""
            Given a valid booking id
            When DELETE request is sent to /bookings/{id}
            Then return a 204 No Content status
            """)
    @WithJwtMockUser
    void cancelBooking_validBookingId_returnNoContentStatus() throws Exception {
        // When
        mockMvc.perform(delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("""
            Given an id of a canceled booking
            When DELETE request is sent to /bookings/{id}
            Then return a 400 Bad Request status
            """)
    @WithJwtMockUser
    void cancelBooking_canceledBookingId_returnBadRequestStatus() throws Exception {
        // When
        mockMvc.perform(delete("/bookings/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
