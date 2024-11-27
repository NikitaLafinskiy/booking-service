package com.booking.bookingservice.domain.auth.service;

import com.booking.bookingservice.domain.auth.dto.LoginResponseDto;
import com.booking.bookingservice.domain.auth.dto.LoginUserRequestDto;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;
import com.booking.bookingservice.domain.user.dto.UserDto;

public interface AuthService {
    LoginResponseDto login(LoginUserRequestDto loginUserRequestDto);

    UserDto register(RegisterUserRequestDto registerUserRequestDto);
}
