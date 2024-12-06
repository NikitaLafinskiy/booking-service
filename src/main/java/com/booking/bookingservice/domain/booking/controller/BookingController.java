package com.booking.bookingservice.domain.booking.controller;

import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.CreateBookingRequestDto;
import com.booking.bookingservice.domain.booking.dto.UpdateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.booking.service.BookingService;
import com.booking.bookingservice.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Create a booking", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Booking created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request body"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
    })
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingDto createBooking(
            @RequestBody @Valid CreateBookingRequestDto createBookingRequestDto,
            @AuthenticationPrincipal UserDto userDto) {
        return bookingService.createBooking(createBookingRequestDto, userDto);
    }

    @Operation(summary = "Get all bookings by user id", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Bookings retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingDto> getUserBookings(@RequestParam Long userId,
                                            @RequestParam Booking.BookingStatus status) {
        return bookingService.getUserBookings(userId, status);
    }

    @Operation(summary = "Get current user's bookings", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Bookings retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
    })
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<BookingDto> getMyBookings(@AuthenticationPrincipal UserDto userDto) {
        return bookingService.getMyBookings(userDto);
    }

    @Operation(summary = "Update a booking", responses = {
            @ApiResponse(responseCode = "200",
                    description = "Booking updated successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request body"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @ApiResponse(responseCode = "404",
                    description = "Booking not found"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingDto updateBooking(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookingRequestDto updateBookingRequestDto,
            @AuthenticationPrincipal UserDto userDto) {
        return bookingService.updateBooking(id, updateBookingRequestDto, userDto);
    }

    @Operation(summary = "Cancel a booking", responses = {
            @ApiResponse(responseCode = "204",
                    description = "Booking canceled successfully"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized"),
            @ApiResponse(responseCode = "400",
                    description = "Booking has already been canceled"),
            @ApiResponse(responseCode = "404",
                    description = "Booking not found"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}
