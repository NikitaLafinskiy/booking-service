package com.booking.bookingservice.security.filter;

import static com.booking.bookingservice.utils.AccommodationTestUtils.setUpMutateAccommodationRequestDto;
import static com.booking.bookingservice.utils.SecurityTestUtils.setUpAccessToken;
import static com.booking.bookingservice.utils.SecurityTestUtils.setUpRefreshToken;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.booking.bookingservice.config.WithJwtMockUser;
import com.booking.bookingservice.domain.accommodation.dto.MutateAccommodationRequestDto;
import com.booking.bookingservice.domain.security.filter.JwtAuthenticationFilter;
import com.booking.bookingservice.domain.token.service.TokenService;
import com.booking.bookingservice.exception.handler.ExceptionHandlerFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest()
@Testcontainers
public class JwtAuthenticationFilterTest {
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context,
                          @Autowired JwtAuthenticationFilter jwtAuthenticationFilter,
                          @Autowired ExceptionHandlerFilter exceptionHandlerFilter) {
        mockMvc = webAppContextSetup(context)
                .addFilter(exceptionHandlerFilter)
                .addFilter(jwtAuthenticationFilter)
                .build();
    }

    @Test
    @DisplayName("""
            Given no authorization header
            When POST request is sent to a protected endpoint (/accommodations)
            Then return 401 Unauthorized
            """)
    void postAccommodations_noAuthorizationHeader_returnUnauthorized() throws Exception {
        // Given
        MutateAccommodationRequestDto mutateAccommodationRequestDto =
                setUpMutateAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(mutateAccommodationRequestDto);

        // When
        mockMvc.perform(post("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            Given an invalid authorization header
            When POST request is sent to a protected endpoint (/accommodations)
            Then return 401 Unauthorized
            """)
    void postAccommodation_invalidAuthorizationHeader_returnUnauthorized() throws Exception {
        // Given
        MutateAccommodationRequestDto mutateAccommodationRequestDto =
                setUpMutateAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(mutateAccommodationRequestDto);

        // When
        mockMvc.perform(post("/accommodations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("""
            Given a valid authorization header
            When POST request is sent to a protected endpoint (/accommodations)
            Then return 200 OK
            """)
    @WithJwtMockUser(isAdmin = true)
    void postAccommodation_validAuthorizationHeader_returnOk() throws Exception {
        // Given
        MutateAccommodationRequestDto mutateAccommodationRequestDto =
                setUpMutateAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(mutateAccommodationRequestDto);

        String accessToken = setUpAccessToken(tokenService);

        // When
        mockMvc.perform(post("/accommodations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            Given an invalid access token and a valid refresh token
            When POST request is sent to a protected endpoint (/accommodations)
            Then return 401 Unauthorized and refresh tokens
            """)
    @WithJwtMockUser(isAdmin = true)
    void postAccommodation_invalidAccessTokenValidRefreshToken_returnUnauthorizedRefreshTokens()
            throws Exception {
        // Given
        MutateAccommodationRequestDto mutateAccommodationRequestDto =
                setUpMutateAccommodationRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(mutateAccommodationRequestDto);

        String refreshToken = setUpRefreshToken(tokenService);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/accommodations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                        .header(REFRESH_TOKEN_HEADER, "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andReturn();

        // Then
        assertNotNull(
                mvcResult.getResponse()
                .getHeader(REFRESH_TOKEN_HEADER));
        assertNotNull(
                mvcResult.getResponse()
                .getHeader(HttpHeaders.AUTHORIZATION));
    }
}
