package com.booking.bookingservice.domain.user.service.impl;

import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    public UserDto me(UserDto userDto) {
        return userDto;
    }
}
