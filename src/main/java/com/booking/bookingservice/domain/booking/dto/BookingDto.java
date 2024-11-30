package com.booking.bookingservice.domain.booking.dto;

import com.booking.bookingservice.domain.accommodation.dto.AccommodationDto;
import com.booking.bookingservice.domain.booking.model.Booking;
import com.booking.bookingservice.domain.user.dto.UserDto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private AccommodationDto accommodation;
    private UserDto user;
    private Booking.BookingStatus status;
}
