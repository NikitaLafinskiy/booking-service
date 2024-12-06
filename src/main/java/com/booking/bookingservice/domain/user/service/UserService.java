package com.booking.bookingservice.domain.user.service;

import com.booking.bookingservice.domain.user.dto.UpdateUserRequestDto;
import com.booking.bookingservice.domain.user.dto.UpdateUserRolesRequestDto;
import com.booking.bookingservice.domain.user.dto.UserDto;

public interface UserService {
    UserDto me(UserDto userDto);

    UserDto updateMe(UserDto userDto, UpdateUserRequestDto updateUserRequestDto);

    UserDto updateUserRoles(Long userId, UpdateUserRolesRequestDto updateUserRolesRequestDto);
}
