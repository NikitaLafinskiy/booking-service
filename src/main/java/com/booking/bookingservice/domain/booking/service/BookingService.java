package com.booking.bookingservice.domain.booking.service;

import com.booking.bookingservice.domain.booking.dto.BookingDto;
import com.booking.bookingservice.domain.booking.dto.MutateBookingRequestDto;
import com.booking.bookingservice.domain.user.dto.UserDto;

public interface BookingService {
    BookingDto createBooking(MutateBookingRequestDto mutateBookingRequestDto,
                             UserDto userDto);
}
