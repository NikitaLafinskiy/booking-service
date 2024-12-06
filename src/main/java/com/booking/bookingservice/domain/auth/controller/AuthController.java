package com.booking.bookingservice.domain.auth.controller;

import com.booking.bookingservice.domain.auth.dto.LoginResponseDto;
import com.booking.bookingservice.domain.auth.dto.LoginUserRequestDto;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;
import com.booking.bookingservice.domain.auth.service.AuthService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Login",
            description = "Login with email and password",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Login successful"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request body"),
            }
    )
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginUserRequestDto loginUserRequestDto) {
        return authService.login(loginUserRequestDto);
    }

    @Operation(summary = "Register",
            description = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "User registered successfully"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request body"),
            }
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Valid RegisterUserRequestDto registerUserRequestDto) {
        return authService.register(registerUserRequestDto);
    }
}
