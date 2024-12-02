package com.booking.bookingservice.domain.booking.controller;

import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.CreateBookingRequestDto;
import com.booking.bookingservice.domain.booking.dto.UpdateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.booking.service.BookingService;
import com.booking.bookingservice.domain.user.dto.UserDto;
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

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingDto createBooking(
            @RequestBody @Valid CreateBookingRequestDto createBookingRequestDto,
            @AuthenticationPrincipal UserDto userDto) {
        return bookingService.createBooking(createBookingRequestDto, userDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingDto> getUserBookings(@RequestParam Long userId,
                                            @RequestParam Booking.BookingStatus status) {
        return bookingService.getUserBookings(userId, status);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<BookingDto> getMyBookings(@AuthenticationPrincipal UserDto userDto) {
        return bookingService.getMyBookings(userDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingDto updateBooking(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookingRequestDto updateBookingRequestDto,
            @AuthenticationPrincipal UserDto userDto) {
        return bookingService.updateBooking(id, updateBookingRequestDto, userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
