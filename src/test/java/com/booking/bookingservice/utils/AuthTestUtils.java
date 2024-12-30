package com.booking.bookingservice.utils;

import com.booking.bookingservice.domain.auth.dto.LoginResponseDto;
import com.booking.bookingservice.domain.auth.dto.LoginUserRequestDto;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;
import com.booking.bookingservice.domain.user.dto.UserDto;

public class AuthTestUtils {
    public static LoginUserRequestDto setUpLoginUserRequestDto() {
        return new LoginUserRequestDto()
                .setEmail("user@gmail.com")
                .setPassword("password");
    }

    public static LoginResponseDto setUpLoginResponseDto() {
        return new LoginResponseDto("accessToken", "refreshToken");
    }

    public static RegisterUserRequestDto setUpRegisterUserRequestDto() {
        return new RegisterUserRequestDto()
                .setEmail("newuser@gmail.com")
                .setFirstName("New")
                .setLastName("User")
                .setPassword("password")
                .setConfirmPassword("password");
    }

    public static UserDto setUpNonExistantUserDto() {
        return new UserDto()
                .setEmail("newuser@gmail.com")
                .setFirstName("New")
                .setLastName("User");
    }
}
