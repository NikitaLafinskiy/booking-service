package com.booking.bookingservice.domain.user.controller;

import com.booking.bookingservice.domain.user.dto.UpdateUserRequestDto;
import com.booking.bookingservice.domain.user.dto.UpdateUserRolesRequestDto;
import com.booking.bookingservice.domain.user.dto.UserDto;
import com.booking.bookingservice.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by id",
            description = "Get user details by user id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User details retrieved successfully"),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized"),
            })
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public UserDto me(@AuthenticationPrincipal UserDto userDto) {
        return userService.me(userDto);
    }

    @Operation(summary = "Update user details",
            description = "Update user details",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User details updated successfully"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request body"),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized"),
            })
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public UserDto updateMe(@AuthenticationPrincipal UserDto userDto,
                            @RequestBody @Valid UpdateUserRequestDto updateUserRequestDto) {
        return userService.updateMe(userDto, updateUserRequestDto);
    }

    @Operation(summary = "Update user roles",
            description = "Update user roles by user id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "User roles updated successfully"),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid request body"),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized"),
            })
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateRole(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUserRolesRequestDto updateUserRolesRequestDto) {
        return userService.updateUserRoles(id, updateUserRolesRequestDto);
    }
}
