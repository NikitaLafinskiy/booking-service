package com.booking.bookingservice.domain.user.service;

import com.booking.bookingservice.domain.user.dto.UserDto;

public interface UserService {
    UserDto me(UserDto userDto);
}
