package com.booking.bookingservice.domain.user.controller;

import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public UserDto me(@AuthenticationPrincipal UserDto userDto) {
        return userService.me(userDto);
    }
}
