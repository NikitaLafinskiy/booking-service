package com.booking.bookingservice.auth.controller;

import static com.booking.bookingservice.utils.AuthTestUtils.setUpLoginResponseDto;
import static com.booking.bookingservice.utils.AuthTestUtils.setUpLoginUserRequestDto;
import static com.booking.bookingservice.utils.AuthTestUtils.setUpNonExistantUserDto;
import static com.booking.bookingservice.utils.AuthTestUtils.setUpRegisterUserRequestDto;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.booking.bookingservice.domain.auth.dto.LoginResponseDto;
import com.booking.bookingservice.domain.auth.dto.LoginUserRequestDto;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;
import com.booking.bookingservice.domain.token.service.TokenService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Sql(scripts = {"classpath:scripts/user/delete-users.sql"})
@Sql(scripts = {"classpath:scripts/user/insert-user.sql"})
public class AuthControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TokenService tokenService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("""
            Given a valid request body and not authorization header
            When POST request is sent to /auth/login
            Then return a LoginResponseDto
            """)
    void login_validRequestBodyAndNoAuthorizationHeader_returnLoginResponseDto() throws Exception {
        // Given
        LoginUserRequestDto loginUserRequestDto = setUpLoginUserRequestDto();
        LoginResponseDto expected = setUpLoginResponseDto();

        when(tokenService.generateToken(any(),
                eq(TokenService.TokenType.ACCESS)))
                .thenReturn(expected.accessToken());
        when(tokenService.generateToken(any(),
                eq(TokenService.TokenType.REFRESH)))
                .thenReturn(expected.refreshToken());

        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        LoginResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                LoginResponseDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("""
            Given an invalid request body
            When POST request is sent to /auth/login
            Then return 400 Bad Request
            """)
    void login_invalidRequestBody_returnBadRequest() throws Exception {
        // Given
        LoginUserRequestDto loginUserRequestDto = new LoginUserRequestDto();

        // When
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequestDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            Given a valid request body
            When POST request is sent to /auth/register
            Then return a UserDto
            """)
    void register_validRequestBody_returnUserDto() throws Exception {
        // Given
        RegisterUserRequestDto registerUserRequestDto = setUpRegisterUserRequestDto();
        UserDto expected = setUpNonExistantUserDto();

        // When
        MvcResult mvcResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserRequestDto))
                )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        UserDto actual = objectMapper.readValue(
                mvcResult.getResponse()
                        .getContentAsString(),
                UserDto.class);

        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @DisplayName("""
            Given a password mismatch
            When POST request is sent to /auth/register
            Then return 400 Bad Request
            """)
    void register_passwordMismatch_returnBadRequest() throws Exception {
        // Given
        RegisterUserRequestDto registerUserRequestDto = setUpRegisterUserRequestDto();
        registerUserRequestDto.setConfirmPassword("other");
        String jsonRequest = objectMapper.writeValueAsString(registerUserRequestDto);

        // When
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andExpect(status().isBadRequest());
    }
}
