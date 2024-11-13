package com.booking.bookingservice.domain.auth.service;

import com.booking.bookingservice.domain.auth.dto.LoginResponseDto;
import com.booking.bookingservice.domain.auth.dto.LoginUserRequestDto;
import com.booking.bookingservice.domain.auth.dto.RegisterUserRequestDto;

public interface AuthService {
    LoginResponseDto login(LoginUserRequestDto loginUserRequestDto);

    void register(RegisterUserRequestDto registerUserRequestDto);
}
