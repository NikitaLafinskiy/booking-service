package com.booking.bookingservice.domain.booking.service;

import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.CreateBookingRequestDto;
import com.booking.bookingservice.domain.booking.dto.UpdateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.user.dto.UserDto;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingRequestDto createBookingRequestDto,
                             UserDto userDto);

    List<BookingDto> getUserBookings(Long userId, Booking.BookingStatus status);

    List<BookingDto> getMyBookings(UserDto userDto);

    BookingDto updateBooking(Long id,
                             UpdateBookingRequestDto updateBookingRequestDto,
                             UserDto userDto);

    void deleteBooking(Long id);
}
