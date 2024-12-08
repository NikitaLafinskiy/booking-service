package com.booking.bookingservice.accommodation.controller;

import static com.booking.bookingservice.utils.AccommodationTestUtils.setUpAccommodationDto;
import static com.booking.bookingservice.utils.AccommodationTestUtils.setUpAccommodationDtos;
import static com.booking.bookingservice.utils.AccommodationTestUtils.setUpMutateAccommodationRequestDto;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.booking.bookingservice.config.WithJwtMockUser;
import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:scripts/accommodation/delete-accommodations.sql")
@Sql(scripts = "classpath:scripts/accommodation/insert-2-accommodations.sql")
@Testcontainers
public class AccommodationControllerTest {
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
            When POST request is sent to /accommodations
            Then return an Accommodation DTO
            """)
    @WithJwtMockUser(isAdmin = true)
    void createAccommodation_validRequestBody_returnAccommodationDto() throws Exception {
        // Given
        AccommodationDto expected = setUpAccommodationDto();
        MutateAccommodationRequestDto mutateAccommodationRequestDto =
                setUpMutateAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(mutateAccommodationRequestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        AccommodationDto actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                AccommodationDto.class);
        actual.getAmenities().sort(String::compareTo);
        boolean isEqual = EqualsBuilder.reflectionEquals(expected,
                actual,
                "id");
        assertTrue(isEqual);
    }

    @Test
    @DisplayName("""
            Given an invalid request body
            When POST request is sent to /accommodations
            Then return a 500 Internal Server Error status
            """)
    @WithJwtMockUser(isAdmin = true)
    void createAccommodation_invalidRequestBody_returnInternalServerError() throws Exception {
        // Given
        MutateAccommodationRequestDto mutateAccommodationRequestDto =
                new MutateAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(mutateAccommodationRequestDto);

        // When
        mockMvc.perform(post("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("""
            When a get request is made to /accommodations
            Then return a list of Accommodation DTOs
            """)
    void getAccommodations_returnListOfAccommodationDtos() throws Exception {
        // Given
        List<AccommodationDto> expected = setUpAccommodationDtos();

        // When
        MvcResult mvcResult = mockMvc.perform(get("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<AccommodationDto> actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                new TypeReference<>() {}
        );

        actual.forEach(accommodationDto -> accommodationDto
                .getAmenities()
                .sort(String::compareTo));

        for (int i = 0; i < expected.size(); i++) {
            assertTrue(EqualsBuilder.reflectionEquals(expected.get(i),
                    actual.get(i),
                    "id"));
        }
    }

    @Test
    @DisplayName("""
            Given an accommodation ID
            When a get request is made to /accommodations/{id}
            Then return an Accommodation DTO
            """)
    void getAccommodation_returnAccommodationDto() throws Exception {
        // Given
        AccommodationDto expected = setUpAccommodationDtos().getFirst();

        // When
        MvcResult mvcResult = mockMvc.perform(get("/accommodations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        AccommodationDto actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                AccommodationDto.class);
        actual.getAmenities().sort(String::compareTo);
        boolean isEqual = EqualsBuilder.reflectionEquals(expected,
                actual,
                "id");
        assertTrue(isEqual);
    }

    @Test
    @DisplayName("""
            Given a valid request body and accommodation ID
            When PUT request is sent to /accommodations/{id}
            Then return an updated Accommodation DTO
            """)
    @WithJwtMockUser(isAdmin = true)
    void updateAccommodation_validRequestBody_returnUpdatedAccommodationDto() throws Exception {
        // Given
        AccommodationDto expected = setUpAccommodationDto();
        MutateAccommodationRequestDto mutateAccommodationRequestDto =
                setUpMutateAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(mutateAccommodationRequestDto);

        // When
        MvcResult mvcResult = mockMvc.perform(put("/accommodations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        AccommodationDto actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                AccommodationDto.class);
        actual.getAmenities().sort(String::compareTo);
        boolean isEqual = EqualsBuilder.reflectionEquals(expected,
                actual,
                "id");
        assertTrue(isEqual);
    }
}
