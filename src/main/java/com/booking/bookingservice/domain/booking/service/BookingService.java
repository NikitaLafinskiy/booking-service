package com.booking.bookingservice.domain.booking.service;

import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.MutateBookingRequestDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.user.dto.UserDto;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(MutateBookingRequestDto mutateBookingRequestDto,
                             UserDto userDto);

    List<BookingDto> getUserBookings(Long userId, Booking.BookingStatus status);

    List<BookingDto> getMyBookings(UserDto userDto);

    BookingDto updateBooking(Long id, MutateBookingRequestDto mutateBookingRequestDto);
}
